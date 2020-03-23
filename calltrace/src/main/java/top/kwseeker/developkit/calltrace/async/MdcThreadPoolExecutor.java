package top.kwseeker.developkit.calltrace.async;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池父子线程中传递MDC
 *
 * 同样在子线程被创建之前提取，在子线程被创建之后设值（需要清楚线程池是什么时候完成创建子线程的）
 *
 * TODO: 除了当前这种写法，应该还可以借助AspectJ实现切面（run() 和 call()），在submit/execute时从父线程提取MDC，并包装原来的run和call方法添加MDC设值操作
 */
public class MdcThreadPoolExecutor extends ThreadPoolExecutor {

    public MdcThreadPoolExecutor(int i, int i1, long l, TimeUnit timeUnit) {
        this(i, i1, l, timeUnit, new LinkedBlockingQueue<>());
    }

    public MdcThreadPoolExecutor(int i, int i1, long l, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue) {
        super(i, i1, l, timeUnit, blockingQueue);
    }

    private Map<String, String> getContextTraceMap() {
        //TODO： 添加父线程名信息跟踪, 并放在日志的 %X{sourceThread} 占位符中
        return MDC.getCopyOfContextMap();
    }

    @Override
    public void execute(Runnable runnable) {
        super.execute(taskWithMdc(runnable, getContextTraceMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return super.submit(taskWithMdc(callable, getContextTraceMap()));
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return super.submit(taskWithMdc(runnable, getContextTraceMap()));
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        return super.submit(taskWithMdc(runnable, getContextTraceMap()), t);
    }

    private <T> Callable<T> taskWithMdc(Callable<T> task, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return task.call();
            } finally {
                MDC.clear();
            }
        };
    }

    private Runnable taskWithMdc(Runnable task, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            task.run();
            MDC.clear();
        };
    }
}
