package top.kwseeker.developkit.logprinter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.developkit.logprinter.service.LogService;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            es.submit(() -> {
                while (true) {
                    int count = counter.addAndGet(1);
                    log.debug("debug log {}", count);
                    log.info("info log {}", count);
                    log.warn("warn log {}", count);
                    log.error("error log {}", count);
                    Thread.sleep(1);
                }
            });
        }
        Thread.currentThread().wait();
    }
}
