package top.kwseeker.developkit.logprinter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.developkit.logprinter.service.x.XService;

import javax.annotation.Resource;

@Slf4j
@Service
public class LogService {

    @Resource
    private XService xService;

    public void printLog() {
        log.debug("[LogService] debug log 1");
        log.info("[LogService] info log 1");
        log.warn("[LogService] warn log 1");
        log.error("[LogService] error log 1");
        xService.printLog();
    }
}
