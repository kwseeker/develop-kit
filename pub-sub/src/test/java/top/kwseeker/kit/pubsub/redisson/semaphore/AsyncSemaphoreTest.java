package top.kwseeker.kit.pubsub.redisson.semaphore;

import org.junit.Test;

/**
 * AsyncSemaphore 异步信号量, TODO 这个Async体现在哪里？
 *
 * //许可数量使用AtomicInteger表示
 * private final AtomicInteger counter;
 * //任务队列，tryAcquire是提交了个countDown()的任务，用于获取acquire()的执行结果
 * private final Queue<Runnable> listeners = new ConcurrentLinkedQueue<>();
 * //暂时没太明白，这个是做什么用的
 * private final Set<Runnable> removedListeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
 *
 * tryAcquire() 调用acquire(), 如果任务执行异常，或获取许可失败会被加入removedListeners
 *
 * acquire() 先将任务加入队列listeners，然后获取许可（decrementAndGet()>=0），
 *      成功，则从队列listeners取出任务，取任务失败则归还许可直接退出，
 *                                  如果任务在removedListeners存在(之前由于获取许可超时或者执行异常的任务)，则归还许可并递归执行tryRun()
 *                                  取任务成功直接执行， !!! 这里执行完没有释放许可，要释放许可只能手动调用 remove() 或 release()
 *      失败，则incrementAndGet()
 *
 */
public class AsyncSemaphoreTest {

    @Test
    public void testAsyncSemaphore() throws InterruptedException {
        ClientConnectionsEntry entry = new ClientConnectionsEntry(64);

        Runnable createConnTask = () -> {
            try {
                System.out.println("create new connection begin ... " + Thread.currentThread().getName());
                Thread.sleep(1000);
                System.out.println("create new connection succeed " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable bizTask = () -> {
            entry.acquireConnection(createConnTask);
            System.err.println("free amount(connection): " + entry.getFreeAmount());
            //使用此连接执行的业务代码
            try {
                System.out.println("do something biz ...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //释放连接，以及释放信号量许可
            entry.releaseConnection();
            System.out.println("biz done, exit");
        };

        Thread thread1 = new Thread(bizTask, "thread-1");
        Thread thread2 = new Thread(bizTask, "thread-2");
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.err.println("free amount: " + entry.getFreeAmount());
    }
}
