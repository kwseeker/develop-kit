# Redisson分布式锁实现原理

[官方中文文档]([https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95](https://github.com/redisson/redisson/wiki/目录))

最早接触`Redisson`是用到其分布式锁功能，但是`Redisson`并不是只提供分布式锁功能，分布式锁只是其中一个功能，它的定位是一个具有分布式特性的工具集。

而且`Redission`分开源版本和Pro版本(付费版本，提供了更多功能)。

**[Table of Content](https://github.com/redisson/redisson/wiki/Table-of-Content)** | [目录](https://github.com/redisson/redisson/wiki/目录)

1. **[Overview](https://github.com/redisson/redisson/wiki/1.-Overview)**
   [概述](https://github.com/redisson/redisson/wiki/1.-概述)
2. **[Configuration](https://github.com/redisson/redisson/wiki/2.-Configuration)**
   [配置方法](https://github.com/redisson/redisson/wiki/2.-配置方法)
3. **[Operations execution](https://github.com/redisson/redisson/wiki/3.-operations-execution)**
   [程序接口调用方式](https://github.com/redisson/redisson/wiki/3.-程序接口调用方式)
4. **[Data serialization](https://github.com/redisson/redisson/wiki/4.-data-serialization)**
   [数据序列化](https://github.com/redisson/redisson/wiki/4.-数据序列化)
5. **[Data partitioning (sharding)](https://github.com/redisson/redisson/wiki/5.-data-partitioning-(sharding))**
   [单个集合数据分片（Sharding）](https://github.com/redisson/redisson/wiki/5.-单个集合数据分片（Sharding）)
6. **[Distributed objects](https://github.com/redisson/redisson/wiki/6.-distributed-objects)**
   [分布式对象](https://github.com/redisson/redisson/wiki/6.-分布式对象)
7. **[Distributed collections](https://github.com/redisson/redisson/wiki/7.-distributed-collections)**
   [分布式集合](https://github.com/redisson/redisson/wiki/7.-分布式集合)
8. **[Distributed locks and synchronizers](https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers)**
   [分布式锁和同步器](https://github.com/redisson/redisson/wiki/8.-分布式锁和同步器)
9. **[Distributed services](https://github.com/redisson/redisson/wiki/9.-distributed-services)**
   [分布式服务](https://github.com/redisson/redisson/wiki/9.-分布式服务)
10. **[Additional features](https://github.com/redisson/redisson/wiki/10.-additional-features)**
    [额外功能](https://github.com/redisson/redisson/wiki/10.-额外功能)
11. **[Redis commands mapping](https://github.com/redisson/redisson/wiki/11.-Redis-commands-mapping)**
    [Redis命令和Redisson对象匹配列表](https://github.com/redisson/redisson/wiki/11.-Redis命令和Redisson对象匹配列表)
12. **[Standalone node](https://github.com/redisson/redisson/wiki/12.-Standalone-node)**
    [独立节点模式](https://github.com/redisson/redisson/wiki/12.-独立节点模式)
13. **[Tools](https://github.com/redisson/redisson/wiki/13.-Tools)**
    [工具](https://github.com/redisson/redisson/wiki/13.-工具)
14. **[Integration with frameworks](https://github.com/redisson/redisson/wiki/14.-Integration-with-frameworks)**
    [第三方框架整合](https://github.com/redisson/redisson/wiki/14.-第三方框架整合)
15. **[Dependency list](https://github.com/redisson/redisson/wiki/15.-Dependency-list)**
    [项目依赖列表](https://github.com/redisson/redisson/wiki/15.-项目依赖列表)
16. **[FAQ](https://github.com/redisson/redisson/wiki/16.-FAQ)**

这里只分析分布式锁那部分的实现。

## `Redission` 网络通信原理

也可以学习下`Redission`是怎么使用`Netty`的。

+ **源码分析准备工作**

  执行单元测试`RedissionLockTest`，第一步是启动`redis-server`, 通过静态代码块或静态方法做了一些初始化工作(指定`redis-server`执行路径，随机生成一个`/tmp/`下的临时目录，随机分配空闲的端口)。

  ```java
  // 修改运行时环境，单元测试默认环境是window，我这里是Linux
  public static final String redisBinaryPath = System.getProperty("redisBinary", "/opt/redis/bin/redis-server");
  // 拼接出来的启动命令 
  // REDIS LAUNCH OPTIONS: [/opt/redis/bin/redis-server, --dir, /tmp/bd653318-f147-4dac-b270-09fae6e56801, --port, 43873]
  // 然后使用 jdk ProcessBuilder 启动 redis-server
  ```

  可以看到第二个就是单元测试启动的(`RedisProcess`实例)。

  ```
  root     10285  0.1  0.0  64328  3764 pts/1    Sl+  2月23   4:20 bin/redis-server 127.0.0.1:6379
  lee      19956  0.1  0.0  64328  5112 ?        Sl   17:00   0:00 /opt/redis/bin/redis-server *:43873
  ```

  第二步是创建并启动`redis-cli`，获取启动的`redis-server`的地址和端口等配置。然后创建`Redisson`实例（就是客户端实例）。

  ```java
  config = new Config();
  config.useSingleServer().setAddress("redis://127.0.0.1:6379");
  redisson = Redisson.create(config);
  ```

+ **`Redisson`客户端连接**

  参考：[程序接口调用方式](https://github.com/redisson/redisson/wiki/3.-程序接口调用方式)

  客户端创建时有多种类型：

  ![](imgs/Redission客户端类型.png)

  `RedissonClient`、`RedissonReactiveClient`和`RedissonRxClient`实例本身和`Redisson`提供的所有分布式对象都是线程安全的。

  现在仅关注`RedissionClient`。

  客户端接口定义`RedissionClient`方法非常多, 大部分都是`Redission`提供的功能接口。

  客户端实现类`Redission implements RedissonClient`，下面是主要的数据结构。

  ```java
  protected final QueueTransferService queueTransferService = new QueueTransferService();
  protected final EvictionScheduler evictionScheduler;
  protected final WriteBehindService writeBehindService;
  protected final ConnectionManager connectionManager;
  
  protected final ConcurrentMap<Class<?>, Class<?>> liveObjectClassCache = new ConcurrentHashMap<>();
  //连接配置，如IP, Port, 密码, db,主从,哨兵, 集群等配置 ...
  protected final Config config;
  protected final ConcurrentMap<String, ResponseEntry> responses = new ConcurrentHashMap<>();
  ```

  创建连接

  ```java
  public static RedissonClient create(Config config) {
      // 通过config创建连接管理器ConnectionManager, 再创建EvictionScheduler，WriteBehindService
      // TODO 这三个是什么东西, 后面说
      Redisson redisson = new Redisson(config);
  	// 中间省略启用redission referece 功能 ? 额外注册了一个ReferenceCodec的编码器, 估计类似AtomicReference，给原本的客户端拓展些功能？TODO
      ...
      return redisson;
  }
  ```

  

  

  

  + SingleConnectionManager

    + MasterSlaveConnectionManager

      内部通过NETTY EPOLL模型创建了线程池。为什么要引入线程池呢？看后面的众多`Async`接口就明白了：我就算只有一个连接但是也是可能需要执行多任务的。关于Netty EPOLL参考对应仓库。

      对应地建立了管理线程池`EpollEventLoopGroup`和工作线程池。

      ```java
      this.group = new EpollEventLoopGroup(cfg.getNettyThreads(), new DefaultThreadFactory("redisson-netty"));
      this.resolverGroup = cfg.getAddressResolverGroupFactory().create(EpollDatagramChannel.class, DnsServerAddressStreamProviders.platformDefault());
      
      executor = Executors.newFixedThreadPool(threads, new DefaultThreadFactory("redisson"));
      
       this.commandExecutor = new CommandSyncService(this);
       
       initTimer(cfg);
      		timer = new HashedWheelTimer(new DefaultThreadFactory("redisson-timer"), minTimeout, TimeUnit.MILLISECONDS, 1024, false);
              connectionWatcher = new IdleConnectionWatcher(this, config);
              subscribeService = new PublishSubscribeService(this, config);
       initSingleEntry();
      ```

      

****

## `Redisson`分布式锁和同步器

文档是上面第８章节。

分布式锁接口实现`java.util.concurrent.locks.Lock`接口。

`Redisson`几种分布式锁

![](imgs/Redisson分布式类UML.png)

+ **可重入锁（Reentrant Lock）**

  类:`RedissonLock`, 源码单元测试类：`RedissionLockTest`。

  + RLock

    继承JUC Lock接口以及RLockAsync拓展的异步接口，拓展了可以自动释放锁的接口。

  + RExpirable(I)

    + RObject
    + RObjectAsync
    + RExpirableAsync

    集成RObject以及RExpireableAsync接口，拓展了生存周期（过期时间）相关的控制接口。

  + CommandSyncService(C)
    + CommandAsyncService(C)
    + CommandExecutor (I, 客户端命令执行器)
  + LockPubSub

  加锁流程：

  １）通过锁名(key名)计算出Redis槽点，进而获取所在Redis服务实例（多Redis节点才有用）。

  ２）创建批处理操作(用于将操作同步到从库), 然后创建`CommandBatchService`。

  ３）通过`CommandBatchService`异步写`EVAL`命令(Future Promise channel写)。

  ```
      // 加锁逻辑就是这段Lua代码
      "if (redis.call('exists', KEYS[1]) == 0) then " +
      "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
      "redis.call('pexpire', KEYS[1], ARGV[1]); " +
      "return nil; " +
      "end; " +
      "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
      "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
      "redis.call('pexpire', KEYS[1], ARGV[1]); " +
      "return nil; " +
      "end; " +
      "return redis.call('pttl', KEYS[1]);"
  ```

  ４）如果没有设置TTL时间就直接退出。有的话会同步等待超时之后执行释放。



+ **公平锁（FairLock）**

+ **联锁（MultiLock）**
+  **红锁（RedLock）**
+ **读写锁（ReadWriteLock）**
+ **信号量（Semaphore）**

+ **可过期性信号量（PermitExpirableSemaphore）**
+ **闭锁（CountDownLatch）**



## 附录

### 相关知识

+ `Netty NIO`

+ 响应式编程 `Reactive & Rx`

  反应式编程是一种涉及数据流和变化传播的异步编程范例。这意味着可以通过所采用的编程语言轻松地表达静态（例如阵列）或动态（例如事件发射器）数据流。

  