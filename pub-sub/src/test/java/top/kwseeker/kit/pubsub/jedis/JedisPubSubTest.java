package top.kwseeker.kit.pubsub.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisPubSubTest {

    @Test
    public void testPubSub() throws InterruptedException {
        String channelName = "channel1";

        Thread listenThread = new Thread(() -> {
            try (Jedis jedis = new Jedis()) {
                System.out.println("listen thread started");
                Listener listener = new Listener(jedis);
                //订阅处理
                JedisPubSub jedisPubSub = new PubSubHandler();
                //基于信道channel订阅
                listener.listen(jedisPubSub, channelName);    //阻塞
                //寄语模式pattern订阅
                //listener.pListen(jedisPubSub, "chan*"); //阻塞
                System.out.println("listen thread exit");
            }
        });

        Thread publishThread = new Thread(() -> {
            try (Jedis jedis = new Jedis()) {
                System.out.println("publish thread started");
                Publisher publisher = new Publisher(jedis);
                publisher.publish(channelName, "Some message to channel1");
                System.out.println("publish thread exit");
            }
        });

        listenThread.start();
        Thread.sleep(100);
        publishThread.start();

        listenThread.join();
    }

    @Test
    public void testPatternPubSub() {
        try(Jedis jedis = new Jedis()) {

            String channelPattern = "chan*";
            String channelName = "channel1";
            //订阅处理
            JedisPubSub jedisPubSub = new PubSubHandler();
            //绑定订阅处理方法和信道
            jedis.psubscribe(jedisPubSub, channelPattern);

            //发布消息
            jedis.publish(channelName, "Some message to channel1");
        }
    }

    static class Listener {

        private Jedis jedis;

        public Listener(Jedis jedis) {
            this.jedis = jedis;
        }

        public void listen(JedisPubSub jedisPubSub, String channel) {
            //绑定订阅处理方法和信道
            jedis.subscribe(jedisPubSub, channel);
        }

        public void pListen(JedisPubSub jedisPubSub, String pattern) {
            jedis.psubscribe(jedisPubSub, pattern);
        }
    }

    static class Publisher {

        private Jedis jedis;

        public Publisher(Jedis jedis) {
            this.jedis = jedis;
        }

        public void publish(String channel, String message) {
            jedis.publish(channel, message);
        }
    }

    static class PubSubHandler extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            System.out.printf("PubSubHandler received message: %s, on channel [%s]\n", message, channel);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            System.out.printf("PubSubHandler received message: %s, on channel [%s] with pattern[%s]\n", message, channel, pattern);
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            System.out.printf("new listener registered on channel [%s], total %d\n", channel, subscribedChannels);
        }

        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
            System.out.printf("listener unregistered on channel [%s], total %d\n", channel, subscribedChannels);
        }

        @Override
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
            System.out.printf("listener unregistered on pattern [%s], total %d\n", pattern, subscribedChannels);
        }

        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
            System.out.printf("new listener registered on pattern [%s], total %d\n", pattern, subscribedChannels);
        }

        @Override
        public void onPong(String pattern) {
            System.out.println("onPong: " + pattern);
        }
    }
}
