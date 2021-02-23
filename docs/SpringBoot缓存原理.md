# SpringBoot缓存原理

Spring Boot 封装了一个缓存Starter, 用于对接不同的缓存实现。如：REDIS，CAFFEINE 等。

这里主要看SpringBoot对接缓存的实现原理。

[官方Demo](https://spring.io/guides/gs/caching/) ()

[官方文档](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)

## SpringBoot集成缓存方案

即 SpringBoot 如何通过一致的接口集成各种缓存方案。

代码耦合度很低，只需要修改很少的配置就可以切换到其他的缓存实现。

> Cache vs Buffer
>
> Buffer: 缓冲区，是快速实体与慢速实体之间的临时存储，是数据媒介，只能数据只能从缓冲读取一次。
>
> Cache: 缓存，是慢速实体的代理，可以读取多次。

### 注解方式声明缓存

Spring缓存注解

+ @Cacheable (触发缓存填充)

  ```java
  //缓存名称(其实是前缀)
  @AliasFor("cacheNames")
  String[] value() default {};
  @AliasFor("value")
  String[] cacheNames() default {};
  //如果有指定key的话, value和key用::拼接后才是真正的key（SPEL格式）
  String key() default "";
  String keyGenerator() default "";
  //指定使用的CacheManager，可以通过配置多个CacheManager对各个缓存进行定制
  String cacheManager() default "";
  //指定使用的cacheResolver
  String cacheResolver() default "";
  //只有入参符合条件才会缓存（SPEL格式）
  String condition() default "";
  //只有结果符合条件才会缓存（SPEL格式）
  String unless() default "";
  //如果有多个线程尝试获取值是否进行同步，如果同步的话缓存只会更新一次，否则可能更新多次。
  boolean sync() default false;
  ```

  下面的注解的参数类似。

  注意`CacheManager`, 

+ @CacheEvict (触发缓存数据驱逐)

+ @CachePut (更新缓存)

+ @Caching (缓存组合操作)

+ @CacheConfig (类级别的缓存配置共享)

  作用于Class, Class下所有方法均有效果。

+ 自定义注解

JSR-107 注解（Spring 4.1 就已经完全支持了JCache标准注解）

+ @CacheResult
+ @CachePut
+ @CacheRemove
+ @CacheRemoveAll
+ @CacheDefaults

### XML方式声明缓存

略。

### 集成缓存实现方案

可以支持下面的缓存方案。

```java
// spring-boot-autoconfigure 
// org.springframework.boot.autoconfigure.cache
public enum CacheType {
    GENERIC,
    JCACHE,
    EHCACHE,
    HAZELCAST,
    INFINISPAN,
    COUCHBASE,
    REDIS,
    CAFFEINE,
    SIMPLE,
    NONE;
}
```



#### JDK ConcurrentMap-based Cache

#### Ehcache 3.x

#### Caffeine

#### GemFire-based

#### JSR-107 Cache

#### Redis

这个要参考：[Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/2.2.13.RELEASE/reference/html/#reference)

[5.14.1. Support for the Spring Cache Abstraction](https://docs.spring.io/spring-data/redis/docs/2.2.13.RELEASE/reference/html/#redis:support:cache-abstraction) 但是官方文档很简略。

实现在 `org.springframework.data.redis.cache` 下面。

使用参考：

[Spring Boot Redis Cache](https://www.journaldev.com/18141/spring-boot-redis-cache)

[springboot整合spring @Cache和Redis](https://www.cnblogs.com/wenjunwei/p/10779450.html)

但是如果想要挖掘更多的使用方法，看源码吧。

关键是理解`RedisCacheManager`和`RedisCacheConfiguration`的作用。

### 集成多个缓存方案

官方说自行实现适配器，后面可能会添加。

### 关于配置TTL/TTI/数据逐出策略

Spring的缓存抽象并没有提供关于这些功能的配置，需要自行通过缓存库的本地API配置。



## SpringBoot缓存集成原理

即 spring-boot-starter-cache 实现原理。

顺着实现流程看源码步骤：

+ **Redis Cache配置**

  配置类位于 `spring-boot-autoconfigure`的`org.springframework.boot.autoconfigure.cache.CacheProperties`.

  这个类读取所有Spring Cache相关的配置。为支持的每一种方案都定义了一个配置类。

  ```java
  @ConfigurationProperties(
      prefix = "spring.cache"
  )
  ```

  Redis Cache配置在`CacheProperties.Redis`中。

  有下面几种配置

  ```java
  private Duration timeToLive;							//缓存
  private boolean cacheNullValues = true;
  private String keyPrefix;
  private boolean useKeyPrefix = true;
  private boolean enableStatistics;
  ```

  

