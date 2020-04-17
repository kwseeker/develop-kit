package top.kwseeker.developkit.distributedlock.lock.impl;

import top.kwseeker.developkit.distributedlock.lock.DistributedLock;

import java.util.concurrent.TimeUnit;

/**
 * 互斥性：redis SETNX 指令
 * 锁超时：从请求获取锁开始计时，超时退出返回false
 * 阻塞和非阻塞：获取锁失败后自旋等待获取锁
 * 可重入：获取锁计数，计数为０后才真正释放锁
 */
public class RedisOpsDistributedLockImpl implements DistributedLock {



    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        //计时

        //通过setnx获取锁

        //如果失败，进入自旋

        //超时退出

        return false;
    }

    @Override
    public void releaseLock(String key) {

    }
}
