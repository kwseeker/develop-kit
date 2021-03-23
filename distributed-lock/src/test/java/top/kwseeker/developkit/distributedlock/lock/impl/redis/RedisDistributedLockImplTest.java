package top.kwseeker.developkit.distributedlock.lock.impl.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisDistributedLockImplTest {

    @Resource(name = "redisDistributedLock")
    private RedisDistributedLockImpl redisDistributedLock;

    @Test
    void tryGetDistributedLock() throws InterruptedException {
        String lockKey = "redisDLock";
        String requestId = UUID.randomUUID().toString();
        boolean lockRet = redisDistributedLock.tryGetDistributedLock(lockKey, requestId, 3);
        System.out.println("lockRet:" + lockRet);
        Thread thread1 = new Thread(() -> {
            String requestId1 = UUID.randomUUID().toString();
            boolean lockRet1 = redisDistributedLock.tryGetDistributedLock(lockKey, requestId1, 3);
            System.out.println("lockRet1: " + lockRet1);
        });
        Thread thread2 = new Thread(() -> {
            String requestId2 = UUID.randomUUID().toString();
            boolean lockRet2 = redisDistributedLock.tryGetDistributedLockBySpin(lockKey, requestId2, 3, 5);
            System.out.println("lockRet2: " + lockRet2);
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @Test
    void tryGetDistributedLockBySpin() throws InterruptedException {
        String lockKey = "redisDLock";
        String requestId = UUID.randomUUID().toString();
        boolean lockRet = redisDistributedLock.tryGetDistributedLock(lockKey, requestId, 30);
        System.out.println("lockRet:" + lockRet);

        Thread thread1 = new Thread(() -> {
            try {
                try {
                    if (!redisDistributedLock.tryGetDistributedLock(lockKey, requestId)) {
                        System.out.println("获取分布式锁失败");
                        throw new Exception("获取分布式锁失败");
                    }
                    System.out.println("lockRet1: " + "获取分布式锁成功");
                } finally {
                    System.out.println(Thread.currentThread().getName() + "释放锁");
                    redisDistributedLock.releaseDistributedLock(lockKey, requestId);
                }
            } catch (Exception e) {
                System.out.println("外边抓获异常");
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            boolean lockRet2 = redisDistributedLock.tryGetDistributedLockBySpin(lockKey, requestId, 5, 5);
            //boolean lockRet2 = redisDistributedLock.tryGetDistributedLockBySpin(lockKey, requestId, 5, 31);
            System.out.println("lockRet2: " + lockRet2);
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @Test
    void testTryGetDistributedLock() {

    }

    @Test
    void testTryGetDistributedLockBySpin() {
    }

    @Test
    void releaseDistributedLock() {
    }
}