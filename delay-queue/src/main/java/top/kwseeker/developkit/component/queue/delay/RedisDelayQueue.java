package top.kwseeker.developkit.component.queue.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Redis 实现的延迟队列
 * 组成：包括 任务池 、存储按延迟时间排列的任务ID的Bucket1、缓存正在处理的任务ID的Bucket2
 * 原理：工作者线程每隔一段时间执行一次从Bucket轮询获取到期的任务，进行处理，处理成功就将任务删除，
 * 否则下次重新加入到Bucket1, 重试处理，最多重试3次。
 */
@Slf4j
@Component("redisDelayQueue")
public class RedisDelayQueue implements IDelayQueue {

    @Resource
    private RedisBucket redisBucket;
    @Resource
    private RedisTaskPool redisTaskPool;
    @Resource
    private RedisReadyQueue redisReadyQueue;

    @Override
    public void push(DelayTask<Object> task) {
        String bucketKey = task.bucketKey();
        BucketItem item = new BucketItem();
        item.setTaskId(task.getId());
        item.setDelay(task.getDelay());
        redisBucket.add(bucketKey, item);

        String poolKey = task.poolKey();
        redisTaskPool.set(poolKey, task);
    }

    @Override
    public void checkReady(DelayTask.Type type) {
        while (true) {
            String bucketKey = DelayTask.bucketKey(type);
            BucketItem bucketItem = redisBucket.get(bucketKey);
            if (bucketItem == null) {
                return;
            }
            long current = System.currentTimeMillis();
            if (bucketItem.getDelay() > current) {
                return;
            }

            String taskKey = DelayTask.poolKey(type, bucketItem.getTaskId());
            DelayTask<?> task = redisTaskPool.get(taskKey);
            if (task == null) {
                redisBucket.remove(bucketKey, bucketItem);
                continue;
            }

            String readyQueueKey = DelayTask.readyQueueKey(type);
            redisReadyQueue.leftPush(readyQueueKey, task);

            redisBucket.remove(bucketKey, bucketItem);
            redisTaskPool.remove(taskKey);
        }
    }

    @Override
    public DelayTask<Object> pollReady(DelayTask.Type type) {
        return redisReadyQueue.rightPop(DelayTask.readyQueueKey(type));
    }

    @Override
    public void remove(DelayTask<Object> task) {
        redisTaskPool.remove(task.poolKey());
        BucketItem item = new BucketItem();
        item.setTaskId(task.getId());
        item.setDelay(task.getDelay());
        redisBucket.remove(task.bucketKey(), item);
    }
}
