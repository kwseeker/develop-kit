package top.kwseeker.developkit.web.mp.vo;

import lombok.Data;
import top.kwseeker.developkit.web.mp.queue.Message;

@Data
public class MpTaskAddReqVO {

    private Long userId;
    private String bizType;
    private Message message;
}
