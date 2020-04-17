package top.kwseeker.developkit.distributedlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class DistributedLockApplication implements ApplicationRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始设置库存
        stringRedisTemplate.opsForValue().set("stock", 100000 + "");
    }
}
