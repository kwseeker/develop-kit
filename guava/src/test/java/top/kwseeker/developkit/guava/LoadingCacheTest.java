package top.kwseeker.developkit.guava;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

public class LoadingCacheTest {

    private static final Executor executor = Executors.newFixedThreadPool(1);
    private static LoadingCache<Integer, String> cache;

    @BeforeClass
    public static void init() {
        cache = CacheBuilder.newBuilder()
                //最大缓存数量
                .maximumSize(3)
                //最大占用内存大小，通过Weigher自定义，和maximumSize不能同时使用
                //.maximumWeight()
                //定时刷新
                //.refreshAfterWrite(30, TimeUnit.SECONDS)
                //.expireAfterWrite(10, TimeUnit.SECONDS)
                .expireAfterAccess(5, TimeUnit.SECONDS) //和expireAfterWrite() 可以一起使用
                //同步监听元素被移除
                //.removalListener(new RemovalListener<Integer, String>() {   //函数式接口
                //    @Override
                //    public void onRemoval(@Nonnull RemovalNotification<Integer, String> removalNotification) {
                //        System.out.println("evicted: key=" + removalNotification.getKey()
                //                + ", value=" + removalNotification.getValue()
                //                + ", cause=" + removalNotification.getCause().toString());
                //    }
                //})
                //异步监听元素被移除
                .removalListener(RemovalListeners.asynchronous(new RemovalListener<Integer, String>() {   //函数式接口
                    @Override
                    public void onRemoval(@Nonnull RemovalNotification<Integer, String> removalNotification) {
                        System.out.println("evicted: key=" + removalNotification.getKey()
                                + ", value=" + removalNotification.getValue()
                                + ", cause=" + removalNotification.getCause().toString());
                    }
                }, executor))
                //使用弱引用存储键
                .weakKeys()
                //使用软引用存储值
                //.softValues()
                .weakValues()
                //可以同时写缓存的线程数量
                //.concurrencyLevel(4)
                //开启统计功能
                .recordStats()
                .build(new CacheLoader<Integer, String>() {
                    /**
                     * 查询key时没有命中，则执行load方法加载值到本地缓存
                     */
                    @Override
                    @Nonnull
                    public String load(@Nonnull Integer key) throws Exception {
                        System.out.println("load key: " + key);
                        return String.valueOf(key);
                    }

                    /**
                     * 重新加载key的值，默认是借助 load() 方法实现
                     */
                    @Override
                    @Nonnull
                    public ListenableFuture<String> reload(@Nonnull Integer key, @Nonnull String oldValue) throws Exception {
                        System.out.println("reload key: " + key);
                        return super.reload(key, oldValue);
                    }
                });
    }

    @Test
    public void testBasicOperation() throws InterruptedException, ExecutionException {
        cache.put(1, "A");
        assertEquals("A", cache.get(1));
        cache.put(2, "B");
        assertEquals(2, cache.size());
        assertEquals("A", cache.get(1));
        assertEquals("B", cache.get(2));
        //手动重新加载key=1的值
        cache.refresh(1);
        assertEquals("1", cache.get(1));
        assertEquals("3", cache.get(3));
        //查看统计信息
        System.out.println(cache.stats().toString());
        //这时会超出容量上限触发被动移除，加载新的KV之后才检查
        assertEquals("4", cache.get(4));

        Thread.sleep(6000);
        System.out.println("after sleep");
        System.out.println(cache.stats().toString());
        //KV已经全部过期了, 但是并不会主动清除过期的数据
        assertEquals(3, cache.size());

        //缓存清除（或者说淘汰）都是在访问（读、写、手动清除）的时候顺便检查下有没有过期的key、容量是否超出，是的话执行数据清除
        //
        System.out.println(cache.get(1));
        cache.put(1, "aaa");
        //显示清除缓存的某个KV,这里面会附带清除过期的key, 所以下面的size()查到的是0
        cache.invalidate(1);
        assertEquals(0, cache.size());
        System.out.println(cache.stats().toString());
        //由于前面cache.invalidate(1)中会附带清除过期的key,而所有的KV都已过期，所以这时已经是全部清空的状态
        cache.invalidateAll();
        System.out.println(cache.stats().toString());
    }

    @Test
    public void testRecencyQueue() {
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        clq.add("a");
        clq.add("b");
        clq.add("c");
        if (clq.contains("b")) {
            clq.add("b");
        }
        System.out.println(clq.toString());
    }
}

