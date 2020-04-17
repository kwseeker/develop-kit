package top.kwseeker.developkit.distributedlock.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 举例分布式场景下提交订单场景
 */
@RestController
public class CartController {

    @Autowired
    private Redisson redisson;
    private String redissonGoodsKey = "DRLock:goods:0001";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //推荐使用Redission做分布式锁（首选方案）
    @RequestMapping("/submitOrder1")
    public String submitOrderUseRedisson() {
        RLock lock = redisson.getLock(redissonGoodsKey);
        //
        lock.lock();
        try {
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                stock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", stock + "");
                System.out.println("扣减成功，库存stock:" + stock);
            } else {
                System.out.println("扣减失败，库存不足");    //下单失败
            }
        }finally {
            lock.unlock();
        }
        return "done";
    }

    @RequestMapping("/submitOrder2")
    public String submitOrderUseRedisOps() {

        return "done";
    }

    //TODO: 为何不推荐使用Zk实现分布式锁
    @RequestMapping("/submitOrder3")
    public String submitOrderUseZk() {

        return "done";
    }

}
