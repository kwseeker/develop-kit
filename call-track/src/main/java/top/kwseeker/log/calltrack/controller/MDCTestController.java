package top.kwseeker.log.calltrack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.log.calltrack.service.MDCTestService;

@RestController
@RequestMapping("/log/mdc")
public class MDCTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MDCTestService mdcTestService;

    @GetMapping("/test")
    public String mdcTest() {
        log.info("{}- mdcTest() called", getClass().getSimpleName());
        return mdcTestService.sayHello();
    }

    @GetMapping("/testAsync")
    public String mdcAsyncTest() {
        log.info("{}- mdcAsyncTest() called", getClass().getSimpleName());
        return mdcTestService.sayHelloAsync();
    }
}
