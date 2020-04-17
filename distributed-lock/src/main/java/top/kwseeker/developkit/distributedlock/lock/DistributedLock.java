package top.kwseeker.developkit.distributedlock.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    boolean tryLock(String key,long timeout, TimeUnit unit);

    void releaseLock(String key);
}
