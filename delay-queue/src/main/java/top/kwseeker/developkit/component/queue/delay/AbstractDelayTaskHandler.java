package top.kwseeker.developkit.component.queue.delay;

import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.developkit.common.util.json.JSONUtil;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class AbstractDelayTaskHandler {

    @Resource
    private IDelayQueue delayQueue;

    public void handleTask(DelayTask.Type type, ExecutorService es, DelayTaskProcess process) {
        //将ready任务迁移到readyQueue
        delayQueue.checkReady(type);

        //读取readyQueue的任务并处理
        int count = 0;
        DelayTask<Object> job;
        while (true) {
            job = delayQueue.pollReady(type);
            if (job == null) {
                log.info("no task to handle: " + type);
                break;      //为 null 说明暂时没有更多到期（ready）待处理的任务
            }

            //实际业务处理job的代码逻辑, 异步执行
            //final DelayTask<?> finalJob = job;
            process.setDelayTask(job);
            CompletableFuture
                    .supplyAsync(process, es)
                    .exceptionally(e -> {
                        DelayTask<Object> finalJob = process.getDelayTask();
                        if (finalJob.getRetryTimes() < finalJob.getMaxRetry()) {
                            finalJob.incrRetryTimes();
                            //重新加入到延迟队列
                            finalJob.setDelay(finalJob.getDelay() + finalJob.getTtr());
                            delayQueue.push(finalJob);
                        } else {
                            process.exceptionHandle();
                            XxlJobHelper.log("handle mp job failed, job: {}", JSONUtil.toJSONString(finalJob));
                        }
                        return null;
                    });

            count++;
        }
        XxlJobHelper.log("handle mp job, time: {}, result: type:{}, count:{}",  type.getName(), count);
    }
}
