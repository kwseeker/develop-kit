package top.kwseeker.kit.pubsub.redisson.semaphore;

import org.redisson.pubsub.AsyncSemaphore;

public class ClientConnectionsEntry {

    private final AsyncSemaphore freeConnectionsCounter;

    public ClientConnectionsEntry(int poolMaxSize) {
        this.freeConnectionsCounter = new AsyncSemaphore(poolMaxSize);
    }

    public void acquireConnection(Runnable runnable) {
        freeConnectionsCounter.acquire(runnable);
    }

    public void removeConnection(Runnable runnable) {
        freeConnectionsCounter.remove(runnable);
    }

    public void releaseConnection() {
        freeConnectionsCounter.release();
    }

    public void reset() {
        freeConnectionsCounter.removeListeners();
    }

    public int getFreeAmount() {
        return freeConnectionsCounter.getCounter();
    }
}
