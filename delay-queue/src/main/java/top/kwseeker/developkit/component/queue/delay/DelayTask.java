package top.kwseeker.developkit.component.queue.delay;

import lombok.Data;
import lombok.Getter;

/**
 * 延迟队列中的任务
 */
@Data
public class DelayTask<T> {

    private static final String DQ_PREFIX = "dq:";

    //任务类型
    private Type type;
    //任务惟一ID
    private String id;
    //延迟截至时间戳 ms
    private Long delay;
    //执行超时时间(time-to-run)，s
    private Integer ttr = 60;
    //重试计数
    private Integer retryTimes = 0;
    //最大重试次数
    private Integer maxRetry = 2;
    //业务信息
    private T body;

    public static String poolKey(Type type, String id) {
        return DQ_PREFIX + "pool:" + type.getName() + ":" + id;
    }

    public static String bucketKey(Type type) {
        return DQ_PREFIX + "bucket:"+ type.getName();
    }

    public static String readyQueueKey(Type type) {
        return DQ_PREFIX + "rq:" + type.getName();
    }

    public String poolKey() {
        return DQ_PREFIX + "pool:" + type.getName() + ":" + id;
    }

    public String bucketKey() {
        return DQ_PREFIX + "bucket:" + type.getName();
    }

    public void incrRetryTimes() {
        retryTimes++;
    }

    @Getter
    public enum Type {
        DEFAULT("dft"),
        MESSAGE_PUSH("mp");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public Type get(String name) {
            return Type.valueOf(name);
        }
    }
}
