# Guava Caches

参考 

+ [Caches](https://github.com/google/guava/wiki/CachesExplained#caches)

+ [Guava 官方教程 - 缓存](https://wizardforcel.gitbooks.io/guava-tutorial/content/13.html)

## 简介

缓存在很多场景下都是相当有用的。例如，计算或检索一个值的代价很高，并且对同样的输入需要不止一次获取值的时候，就应当考虑使用缓存。

`Guava Cache`适用于：

- 你愿意消耗一些内存空间来提升速度。
- 你预料到某些键会被查询一次以上。
- 缓存中存放的数据总量不会超出内存容量。（Guava Cache是单个应用运行时的**本地缓存**。它不把数据存放到文件或外部服务器。如果这不符合你的需求，请尝试[Memcached](http://memcached.org/)这类工具）

`Guava Cache` 特性：

+ **线程安全**

+ **支持自定义缓存的最大数量 或 内存最大占用**

+ **支持自定义数据加载 CacheLoader ，包括重新载入**

  key不存在时通过load()方法加载key对应的值到本地缓存，刷新通过refresh()调用CacheLoader的reload()方法实现，reload()方法默认借助load()方法实现刷新。

  支持手动刷新 refresh() 以及定时刷新 refreshAfterWrite(long, TimeUnit)。

+ **支持缓存回收**：基于容量回收、定时回收、基于引用回收

  缓存清除（或者说淘汰）都是在访问（读、写、手动清除）的时候顺便检查下有没有过期的key、容量是否超出，是的话执行数据清除，并没有启用单独的回收线程。

+ **显示插入、清除**

+ **支持数据移除的监听 RemovalListener**

  支持同步与异步的监听。

+ **缓存刷新**

  支持手动刷新和定时刷新。

+ **统计信息**：缓存命中率、加载新值的平均时间、缓存被回收的总数（不包括显示清除）

+ **asMap 视图**， 提供了缓存的ConcurrentMap形式

  得益于Guava CacheBuilder 创建的 LocalCache 继承 JUC 的 ConcurrentMap 接口。

  ```
  LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>
  ```

DEMO测试：

```java
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
                .expireAfterAccess(5, TimeUnit.SECONDS)
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
                //.weakKeys()
                //使用软引用存储值
                //.softValues()
                //.weakValues()
                //并发等级，内部用的分段锁的思想降低并发粒度，其实就是分段数量
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
        //显示清除缓存的某个KV,这里面会附带清除过期的key, 所以下面的size()查到的是0
        cache.invalidate(1);
        assertEquals(0, cache.size());
        System.out.println(cache.stats().toString());
        //由于前面cache.invalidate(1)中会附带清除过期的key,而所有的KV都已过期，所以这时已经是全部清空的状态
        cache.invalidateAll();
        System.out.println(cache.stats().toString());
    }
}
```



## 源码分析

代码也不少暂时没看很细，里面基于引用类型的淘汰策略（Size-based Eviction、Reference-based Eviction），以及并发控制（参考concurrencyLevel()方法）原理还是值得研究下的。

### 数据结构

CacheBuilder.build() 方法创建的是 **LocalLoadingCache**, 继承 **LocalManualCache**, LocalManualCache 内部包含了 **LocalCache** 对象引用，LocalCache是 核心类，LocalCache中分了多个段 **Segment** 存储数据, 段的个数通过 concurrencyLevel maxWeight等参数通过分段算法计算得到，每个段中又有一个**ReferenceEntry**的哈希表，而对于hash冲突的key采用拉链法存储。

当数据量达到最大限制，支持通过LRU算法淘汰数据，LRU的实现依靠 Segment 中的 writeQueue 和 accessQueue。

**分段算法**：

即 分段数是 不小于concurrencyLevel 或者 大于 maxWeight / 20 的最小的2次幂指数。

```java
while (segmentCount < concurrencyLevel && (!evictsBySize() || segmentCount * 20 <= maxWeight)) {
      ++segmentShift;
      segmentCount <<= 1;
}
```

**Segment的扩容**：

扩容因子也是0.75 

```java
void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
    this.threshold = newTable.length() * 3 / 4; // 0.75
    if (!map.customWeigher() && this.threshold == maxSegmentWeight) {
        // prevent spurious expansion before eviction
        this.threshold++;
    }
    this.table = newTable;
}
```

### 淘汰策略原理



### 并发控制原理

