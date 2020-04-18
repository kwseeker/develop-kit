package top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@Component
public class BasicServiceAHystrix implements BasicServiceAClient {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public List<Entity> list() {
        log.warn("list() 进入断路器 ...");
        return null;
    }

    @Override
    public int save() {
        log.warn("save() 进入断路器 ...");
        return 0;
    }
}
