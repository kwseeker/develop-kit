package top.kwseeker.log.calltrack.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 这是和业务相关的代码，强烈不建议在这里操作MDC或添加其他与业务无关的逻辑
 */
public class GreetWork implements Callable<String> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String call() throws Exception {
        log.info("GreetWork#call() called");
        return "hello MDC (in GreetWork)";
    }
}
