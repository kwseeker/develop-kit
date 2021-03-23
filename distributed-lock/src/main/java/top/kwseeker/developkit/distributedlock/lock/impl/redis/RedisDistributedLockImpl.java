package top.kwseeker.developkit.distributedlock.lock.impl.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Redis分布式锁简单实现
 * 分布式锁几个要点：
 *  互斥性　请求锁超时取消　获取锁超时释放　异步续命　可重入　锁服务高可用
 *
 */
@Component("redisDistributedLock")
@Slf4j
public class RedisDistributedLockImpl {

    private static final Long RELEASE_SUCCESS = 1L;
    private static final int EXPIRE_TIME = 5;
    private static final int WAIT_TIME = 10;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final ThreadLocal<Set<String>> lockFlag = new ThreadLocal<>();

    /**
     * 尝试获取分布式锁
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId) {
        return tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME);
    }

    /**
     * 尝试获取分布式锁 自旋支持，超过5秒没有拿到锁依然会返回false
     *
     * @param lockKey
     * @param requestId
     * @return
     */
    public boolean tryGetDistributedLockBySpin(String lockKey, String requestId) {
        return tryGetDistributedLockBySpin(lockKey, requestId, EXPIRE_TIME, EXPIRE_TIME);
    }

    public boolean tryGetDistributedLockBySpin(String lockKey, String requestId, int expireTime, int timeout) {

        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + timeout * 1000L;
        boolean getLock = false;

        //成功获取锁或者超时退出
        while (!getLock && currentTime < endTime) {
            if (tryGetDistributedLock(lockKey, requestId, expireTime)) {
                getLock = true;
            } else {
                //防止高并发下，大量的循环，导致CPU压力过大
                try {
                    Thread.sleep(WAIT_TIME);
                    currentTime = System.currentTimeMillis();
                } catch (InterruptedException e) {
                    log.error("尝试自选获取锁失败，lockKey：" + lockKey + "", e);
                    break;
                }
            }
        }
        return getLock;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        Boolean flag = stringRedisTemplate.execute((RedisCallback<Boolean>) action ->
                action.set(serializer.serialize(lockKey), serializer.serialize(requestId), Expiration.seconds(expireTime), RedisStringCommands.SetOption.SET_IF_ABSENT));
        flag = flag == null ? false : flag;
        if (flag) {
            if (lockFlag.get() == null) {
                lockFlag.set(new HashSet<>(2));
            }
            lockFlag.get().add(requestId);
        }
        return flag;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        if (lockFlag.get() != null && lockFlag.get().size() != 0 && lockFlag.get().contains(requestId)) {
            lockFlag.get().remove(requestId);
            if (lockFlag.get().size() == 0) {
                lockFlag.remove();
            }

            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            RedisScript<Long> redisScript = RedisScript.of(script, Long.class);
            Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);

            return RELEASE_SUCCESS.equals(result);
        }

        return false;
    }
}
