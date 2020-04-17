package top.kwseeker.developkit.distributedlock.lock.impl;

import top.kwseeker.developkit.distributedlock.lock.DistributedLock;

import java.util.concurrent.TimeUnit;

public class EtcdDistributedLockImpl implements DistributedLock {

    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public void releaseLock(String key) {

    }
}
