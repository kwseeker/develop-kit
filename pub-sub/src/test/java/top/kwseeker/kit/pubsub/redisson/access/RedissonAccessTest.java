package top.kwseeker.kit.pubsub.redisson.access;

import org.junit.Assert;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;

import java.util.Collection;
import java.util.List;

/**
 * Redisson 也是 Redis的客户端，也是可以操作Redis中定义的类型的
 * 只不过都是通过Redisson中自定义的一些RObject、RObjectReactive等对象实现的
 * 详细参考：
 *  Redis命令和Redisson对象匹配列表 https://github.com/redisson/redisson/wiki/11.-Redis%E5%91%BD%E4%BB%A4%E5%92%8CRedisson%E5%AF%B9%E8%B1%A1%E5%8C%B9%E9%85%8D%E5%88%97%E8%A1%A8
 */
public class RedissonAccessTest {

    @Test
    public void testAccessRedisServer() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        //ConnectionManager connectionManager = ((Redisson) redisson).getConnectionManager();
        //Codec codec = new SerializationCodec();
        //Codec codec = new JsonJacksonCodec();
        Codec codec = new StringCodec();        //纯字符串编码（无转换），命令行输入的值，需要程序中用这种解码器读取

        //String
        //RBucket<String> stringObject = new RedissonBucket<>(connectionManager.getCommandExecutor(), "key1");
        RBucket<String> stringObject = redisson.getBucket("key1", codec);
        //write
        stringObject.set("A string object");        //注意不指定编码器时默认实际写入的是 "\x04>\x0fA string object" 并不是 "A string object", 因为默认（3.15.0）用了MarshallingCodec编解码器, 和Redis命令行写入时编解码方式有差异
                                                    //换成SerializationCodec的话，实际写入的是"\xac\xed\x00\x05t\x00\x0fA string object"
        //read
        Assert.assertEquals("A string object", stringObject.get());

        //List
        RDeque<String> listObject = redisson.getDeque("key2", codec);
        //write 除了同步写，还是支持异步写
        listObject.addLast("one");
        RFuture<Void> addTwoFuture = listObject.addLastAsync("two");
        addTwoFuture.awaitUninterruptibly();
        //read
        List<String> listValues = listObject.readAll();
        Assert.assertEquals("one", listValues.get(0));
        Assert.assertEquals("two", listValues.get(1));

        //Hash
        RMap<String, String> mapObject = redisson.getMap("key3", codec);
        mapObject.put("A", "a");
        mapObject.put("B", "b");
        Assert.assertEquals("a", mapObject.get("A"));
        Assert.assertEquals("b", mapObject.get("B"));

        //Set
        RSet<String> setObject = redisson.getSet("key4", codec);
        setObject.add("one");
        setObject.add("two");
        Assert.assertTrue(setObject.contains("one"));
        Assert.assertTrue(setObject.contains("two"));

        //ZSet
        RScoredSortedSet<String> zSetObject = redisson.getScoredSortedSet("key5", codec);
        zSetObject.add(1.0, "one");
        zSetObject.add(2.0, "two");
        Collection<String> zSetValues = zSetObject.valueRange(0, -1);
        zSetValues.forEach(System.out::println);

        redisson.shutdown();
    }
}
