package top.kwseeker.developkit.logprinter.service.x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.developkit.logprinter.service.x.y.XYService;

import javax.annotation.Resource;

@Slf4j
@Service
public class XService {

    @Resource
    private XYService xyService;

    public void printLog() {
        log.debug("[XYService] debug log 1");
        log.info("[XYService] info log 1");
        log.warn("[XYService] warn log 1");
        log.error("[XYService] error log 1");
        xyService.printLog();
    }
}
