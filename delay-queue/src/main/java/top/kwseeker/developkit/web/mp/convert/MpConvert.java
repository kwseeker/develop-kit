package top.kwseeker.developkit.web.mp.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.kwseeker.developkit.component.queue.delay.DelayTask;
import top.kwseeker.developkit.web.mp.queue.MpTask;
import top.kwseeker.developkit.web.mp.queue.MpTaskBody;
import top.kwseeker.developkit.web.mp.vo.MpTaskAddReqVO;
import top.kwseeker.developkit.web.mp.vo.MpTaskDeleteReqVO;

//@Mapper(componentModel = "spring")
@Mapper
public interface MpConvert {

    MpConvert INSTANCE = Mappers.getMapper(MpConvert.class);

    MpTaskBody convert(MpTaskAddReqVO reqVO);

    default MpTask convert(MpTaskDeleteReqVO reqVO) {
        DelayTask.Type type = DelayTask.Type.valueOf(reqVO.getType());
        MpTask task = new MpTask();
        task.setId(reqVO.getId());
        task.setType(type);
        task.setDelay(reqVO.getDelay());
        return task;
    }
}
