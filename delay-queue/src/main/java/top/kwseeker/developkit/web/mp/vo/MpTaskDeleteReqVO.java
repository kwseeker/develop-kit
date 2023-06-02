package top.kwseeker.developkit.web.mp.vo;

import lombok.Data;

@Data
public class MpTaskDeleteReqVO {

    private String type;
    //任务惟一ID
    private String id;
    //延迟截至时间戳 ms
    private Long delay;
}
