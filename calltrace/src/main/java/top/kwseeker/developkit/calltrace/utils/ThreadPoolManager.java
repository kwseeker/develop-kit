package top.kwseeker.developkit.calltrace.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Spring中没必要定义线程池管理器管理单例线程池
 */
public class ThreadPoolManager {

    private static volatile ExecutorService executorService;

    private ThreadPoolManager() {
    }

    public static ExecutorService getSingletonESInstance() {
        if(executorService == null) {
            synchronized (ThreadPoolManager.class) {
                if(executorService == null) {
                    executorService = Executors.newFixedThreadPool(1);
                }
            }
        }
        return executorService;
    }
}
