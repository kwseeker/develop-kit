package top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@FeignClient(value = "basic-service-a", fallback = BasicServiceAHystrix.class)
public interface BasicServiceAClient {

    @RequestMapping(value = "/a/list", method = RequestMethod.GET)
    List<Entity> list();

    @RequestMapping(value = "/a/save", method = RequestMethod.GET)
    int save();
}
