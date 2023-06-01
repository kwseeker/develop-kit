package top.kwseeker.developkit.web.mp.schedule;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.kwseeker.developkit.common.util.json.JSONUtil;
import top.kwseeker.developkit.component.queue.delay.AbstractDelayTaskHandler;
import top.kwseeker.developkit.component.queue.delay.DelayTaskProcess;
import top.kwseeker.developkit.component.queue.delay.IDelayQueue;
import top.kwseeker.developkit.component.queue.delay.DelayTask;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * 延迟推送的消息的处理器
 */
@Slf4j
@Component
public class MpJobHandler extends AbstractDelayTaskHandler {

    private final MpJobProcess process = MpJobProcess.getInstance();

    private ExecutorService es;

    @Resource
    private IDelayQueue delayQueue;

    /**
     * 消息推送任务处理器
     * XxlJobSpringExecutor 实例化后通过扫描所有 Bean @XxlJob 注释的方法，
     * 创建 MethodJobHandler 并注册到名为 jobHandlerRepository 的 ConcurrentHashMap 中
     */
    @XxlJob(value = "mpJobHandler", init = "init", destroy = "destroy")
    public void handleMpJob() {
        XxlJobHelper.log("handle mp job ...");

        DelayTask.Type type = DelayTask.Type.MESSAGE_PUSH;

        super.handleTask(type, es, process);
    }

    public void init(){
        log.info("Mp job handler init");
        es = new ThreadPoolExecutor(8, 32, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    public void destroy() throws InterruptedException {
        log.info("Mp job handler destroy");
        es.awaitTermination(30, TimeUnit.SECONDS);
    }

    static class MpJobProcess extends DelayTaskProcess {

        private static final MpJobProcess INSTANCE = new MpJobProcess();

        protected static MpJobProcess getInstance() {
            return INSTANCE;
        }

        @Override
        public void handle() {
            log.info("handle job: {}", JSONUtil.toJSONString(delayTask));
        }
    }
}