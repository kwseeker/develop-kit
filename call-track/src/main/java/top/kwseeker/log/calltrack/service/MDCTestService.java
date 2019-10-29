package top.kwseeker.log.calltrack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kwseeker.log.calltrack.async.GreetWork;
import top.kwseeker.log.calltrack.async.MdcThreadPoolExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Service
public class MDCTestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MdcThreadPoolExecutor mdcThreadPoolExecutor;


    public String sayHello() {
        log.info("{}- sayHello() called", getClass().getSimpleName());
        return "hello MDC";
    }

    public String sayHelloAsync() {
        try {
            log.info("{}- sayHelloByThread() called", getClass().getSimpleName());
            FutureTask<String> futureTask = new FutureTask<>(new GreetWork());

            ////MdcThread (test done)
            //new MdcThread(futureTask).start();

            //MDC的操作封装起来，让外部看不出有MDC的传递
            mdcThreadPoolExecutor.submit(futureTask);
            //这里做些其他可与子线程任务异步执行的工作

            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return "";
        }
    }
}
