package top.kwseeker.developkit.component.queue.delay;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.kwseeker.developkit.common.util.json.JSONUtil;

import javax.annotation.Resource;

@Component
public class RedisReadyQueue {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void leftPush(String readyQueueKey, DelayTask<?> task) {
        stringRedisTemplate.opsForList().leftPush(readyQueueKey, JSONUtil.toJSONString(task));
    }

    public DelayTask<Object> rightPop(String readyQueueKey) {
        String taskJson = stringRedisTemplate.opsForList().rightPop(readyQueueKey);
        return JSONUtil.parseObject(taskJson, DelayTask.class);
    }
}
