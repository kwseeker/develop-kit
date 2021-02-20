package top.kwseeker.developkit.logprinter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.developkit.logprinter.service.LogService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @PostMapping("/printLog")
    public String printLog() {
        log.debug("debug log 1");
        log.info("info log 1");
        log.warn("warn log 1");
        log.error("error log 1");
        logService.printLog();
        return "done";
    }

    @PostMapping("/printExLog")
    public String printExceptionLog() {
        //int j = 1 / 0;
        try {
            int i = 1 / 0;
            return "done";
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("exception: ", e);
        }
        return "";
    }

    @PostMapping("/conPrintLog")
    public void continuousPrintLog() throws Exception {
        int counter = 1;
        while (true) {
            log.debug("debug log {}", counter);
            log.info("info log {}", counter);
            log.warn("warn log {}", counter);
            log.error("error log {}", counter);
            counter ++;
            Thread.sleep(1);
        }
    }
}
