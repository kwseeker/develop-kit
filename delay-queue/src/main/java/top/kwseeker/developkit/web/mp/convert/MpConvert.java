package top.kwseeker.developkit.web.mp.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.kwseeker.developkit.web.mp.queue.MpTaskBody;
import top.kwseeker.developkit.web.mp.vo.MpTaskAddReqVO;

@Mapper
public interface MpConvert {

    MpConvert INSTANCE = Mappers.getMapper(MpConvert.class);

    MpTaskBody convert(MpTaskAddReqVO reqVO);
}
