package top.kwseeker.kit.pubsub.redisson;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import top.kwseeker.kit.pubsub.message.Message;

public class RedissonPubSubTest {

    @Test
    public void testPubSub() throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);

        //消息管道
        RTopic topic = redisson.getTopic("topic-test", new SerializationCodec());

        //注册订阅者
        topic.addListener(Message.class, new MessageListener<Message>() {
            //channel 类型: org.redisson.client.ChannelName, 字段是名为name的byte数组实现CharSequence接口
            @Override
            public void onMessage(CharSequence channelName, Message message) {
                System.out.printf("Message published on channel: %s, message: %s\n", channelName, message.toString());
            }
        });

        //发布者发布消息
        topic.publish(new Message("some message..."));

        Thread.sleep(100000);
    }
}
