package top.kwseeker.developkit.reactive.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxAsyncTest {

    @Test
    public void testFluxAsync() throws InterruptedException {
        Flux
                .range(0, 10)
                //.publishOn(Schedulers.immediate())  // 主线程执行
                //.publishOn(Schedulers.single())  // 单线程异步执行
                //.publishOn(Schedulers.elastic())  // 弹性线程池异步执行
                .publishOn(Schedulers.parallel())  // 并行线程池异步执行（工作者线程数和处理器核心数相同）
                .subscribe(Utils::println);

        Thread.currentThread().join(1000L);
    }
}
