package top.kwseeker.developkit.infrastructure.dao.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    //@Bean
    //public JedisConnectionFactory redisConnectionFactory() {
    //    JedisConnectionFactory factory = new JedisConnectionFactory();
    //    factory.setUsePool(true);
    //    return factory;
    //}
    //
    //@Bean
    //public RedisTemplate redisTemplate() {
    //    RedisTemplate redisTemplate = new RedisTemplate();
    //    redisTemplate.setConnectionFactory(redisConnectionFactory());
    //    return redisTemplate;
    //}
}
