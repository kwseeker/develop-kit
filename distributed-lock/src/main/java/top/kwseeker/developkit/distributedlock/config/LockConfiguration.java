package top.kwseeker.developkit.distributedlock.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfiguration {

    /**
     * Redisson分布式锁
     */
    @Bean
    public Redisson redisson(){
        Config config=new Config();
        config.useSingleServer().setAddress("127.0.0.1:6379");
        config.useSingleServer().setConnectionMinimumIdleSize(1);
        return (Redisson)Redisson.create(config);
    }


}
