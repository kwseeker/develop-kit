package top.kwseeker.developkit.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfigurations {

    @Resource
    private CacheProperties cacheProperties;

    /**
     * 通过定义多个CacheManager可以对接不同的缓存方案(如同时支持 Redis Caffeine)
     * 在注解中通过cacheManager属性指定使用的CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        // 默认配置，没有指定使用哪个RedisCacheConfiguration的cacheName默认使用这个配置
        RedisCacheConfiguration defaultRedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixKeysWith(cacheProperties.getCacheNamePrefix())
                // 2.2.5还不支持这个方法
                //.prefixCacheNameWith(cacheProperties.getCacheNamePrefix())
                .disableCachingNullValues()
                // 默认使用JDK的序列化器JdkSerializationRedisSerializer存到redis后显示乱码，不方便查看内容故改为下面的序列化器，
                // 也可以参考序列化器实现自定义序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        //通过定义多个RedisCacheConfiguration可以对key-value设置多种控制方式（如不同的TTL）通过注解的cacheNames指定。
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        cacheProperties.getCacheConfigMap().forEach((cacheName, duration) -> {
            redisCacheConfigurationMap.put(cacheName, defaultRedisCacheConfiguration.entryTtl(duration));
        });

        return new RedisCacheManager(redisCacheWriter, defaultRedisCacheConfiguration, redisCacheConfigurationMap);
    }
}
