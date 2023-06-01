package top.kwseeker.developkit.component.queue.delay;

import lombok.Data;

@Data
public class BucketItem {

    //任务惟一ID
    private String taskId;
    //延迟时间戳
    private Long delay;
}
