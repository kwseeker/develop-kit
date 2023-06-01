package top.kwseeker.developkit.web.mp.controller;

import cn.hutool.core.util.IdUtil;
import top.kwseeker.developkit.component.queue.delay.DelayTask.Type;

import org.springframework.web.bind.annotation.*;
import top.kwseeker.developkit.component.queue.delay.IDelayQueue;
import top.kwseeker.developkit.web.mp.convert.MpConvert;
import top.kwseeker.developkit.web.mp.queue.MpTask;
import top.kwseeker.developkit.web.mp.queue.MpTaskBody;
import top.kwseeker.developkit.web.mp.vo.MpTaskAddReqVO;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mp")
public class MpController {

    private static final long DELAY_MS = 5 * 60 * 1000L;

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
    }

    @DeleteMapping("/task")
    public void deleteTask() {

    }
}
