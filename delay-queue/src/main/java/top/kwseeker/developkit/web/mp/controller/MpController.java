package top.kwseeker.developkit.web.mp.controller;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.developkit.common.util.json.JSONUtil;
import top.kwseeker.developkit.component.queue.delay.DelayTask.Type;

import org.springframework.web.bind.annotation.*;
import top.kwseeker.developkit.component.queue.delay.IDelayQueue;
import top.kwseeker.developkit.web.mp.convert.MpConvert;
import top.kwseeker.developkit.web.mp.queue.MpTask;
import top.kwseeker.developkit.web.mp.queue.MpTaskBody;
import top.kwseeker.developkit.web.mp.vo.MpTaskAddReqVO;
import top.kwseeker.developkit.web.mp.vo.MpTaskDeleteReqVO;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/mp")
public class MpController {

    private static final long DELAY_MS = 60 * 1000L;

    @Resource
    private IDelayQueue delayQueue;

    @PutMapping("/task")
    public void addTask(@RequestBody MpTaskAddReqVO reqVO) {
        MpTaskBody body = MpConvert.INSTANCE.convert(reqVO);
        MpTask task = new MpTask();
        task.setType(Type.MESSAGE_PUSH);
        task.setId(IdUtil.objectId());
        task.setDelay(System.currentTimeMillis() + DELAY_MS);
        task.setBody(body);

        delayQueue.push(task);
        log.info("task added, task: {}", JSONUtil.toJSONString(task));
    }

    @DeleteMapping("/task")
    public void deleteTask(@RequestBody MpTaskDeleteReqVO reqVO) {
        MpTask task = MpConvert.INSTANCE.convert(reqVO);
        delayQueue.remove(task);
        log.info("task deleted, task: {}", JSONUtil.toJSONString(task));
    }
}
