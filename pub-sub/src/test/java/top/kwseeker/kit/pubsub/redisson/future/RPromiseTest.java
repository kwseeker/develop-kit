package top.kwseeker.kit.pubsub.redisson.future;

import org.junit.Test;
import org.redisson.misc.RPromise;
import org.redisson.misc.RedissonPromise;

/**
 * 初始化Redis服务节点连接信息的代码中Future模式的应用：masterSlaveEntry.setupMasterEntry(new RedisURI(config.getMasterAddress()))
 * 代码剔除业务逻辑就类似下面的代码，只不过trySuccess tryFailure 在setupMasterEntry()中是在异步代码中执行的
 */
public class RPromiseTest {

    @Test
    public void testRPromise() {
        //实例化Netty DefaultPromise, 事件处理器指定为ImmediateEventExecutor (单例对象)
        RPromise<String> promise = new RedissonPromise<>();
        promise.onComplete((res, e) -> {
            if (e != null) {
                System.out.println("promise onComplete: error occur, e=" + e.getMessage());
                return;
            }
            System.out.println("promise onComplete: succeed, res=" + res);
        });

        RPromise<String> future = new RedissonPromise<>();
        future.onComplete((res, e) -> {
            if (e != null) {
                System.out.println("future onComplete: error occur, e=" + e.getMessage());
                promise.tryFailure(e);
                return;
            }
            System.out.println("future onComplete: succeed, res=" + res);
            promise.trySuccess("success");  //trySuccess tryFailure 在异步代码中执行
        });

        new Thread(()-> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //1 测试正常情况
            future.trySuccess("inner success"); //trySuccess tryFailure 在异步代码中执行
            //2 测试异常情况
            //future.tryFailure(new Throwable("something error!"));
        }).start();

        long t1 = System.currentTimeMillis();
        promise.syncUninterruptibly();      //同步阻塞等待
        System.out.println("wait time: " + (System.currentTimeMillis() - t1));
        String ret = promise.getNow();
        System.out.println(ret);
    }
}
