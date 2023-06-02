package top.kwseeker.developkit.component.queue.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import top.kwseeker.developkit.common.util.json.JSONUtil;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 每个业务对应一个bucket, 保存 delay -> taskId
 */
@Slf4j
@Component
public class RedisBucket {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param bucketKey bucket key
     * @param item      delay -> taskId
     */
    public void add(String bucketKey, BucketItem item) {
        stringRedisTemplate.opsForZSet().add(bucketKey, item.getTaskId(), (double) item.getDelay());
    }

    public BucketItem get(String bucketKey) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().rangeWithScores(bucketKey, 0, 0);
        if (typedTuples == null || typedTuples.isEmpty()) {
            return null;
        } else if (typedTuples.size() > 1) {
            log.error("poll 查找到重复元素: bucketKey:{} value: {}", bucketKey, JSONUtil.toJSONString(typedTuples));
            return null;
        }
        BucketItem item = null;
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            item = new BucketItem();
            item.setTaskId(typedTuple.getValue());
            Double score = typedTuple.getScore();
            item.setDelay(score == null ? 0L : Math.round(score));
        }

        return item;
    }

    public void remove(String bucketKey, BucketItem item) {
        double score = item.getDelay();
        stringRedisTemplate.opsForZSet().removeRangeByScore(bucketKey, score, score);
    }
}
