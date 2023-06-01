package top.kwseeker.developkit.web.mp.queue;

import lombok.Data;

/**
 * 消息推送任务主体内容
 */
@Data
public class MpTaskBody {

    private Long userId;
    private String bizType;
    private Message message;
}
