package top.kwseeker.developkit.component.queue.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.kwseeker.developkit.common.util.json.JSONUtil;

import javax.annotation.Resource;

/**
 * 存放延迟队列延迟任务的对象池
 */
@Slf4j
@Component
public class RedisTaskPool {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String taskKey, DelayTask<?> task) {
        stringRedisTemplate.opsForValue().set(taskKey, JSONUtil.toJSONString(task));
    }

    public DelayTask<?> get(String taskKey) {
        String taskJson = stringRedisTemplate.opsForValue().get(taskKey);
        return JSONUtil.parseObject(taskJson, DelayTask.class);
    }

    public void remove(String taskKey) {
        //stringRedisTemplate.opsForValue().getAndDelete(taskKey);  //redis 6.2.0 后才支持
        stringRedisTemplate.delete(taskKey);
    }
}
