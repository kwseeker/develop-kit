package top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@FeignClient(value = "basic-service-b", fallback = BasicServiceBHystrix.class)
public interface BasicServiceBClient {

    @RequestMapping(value = "/b/list", method = RequestMethod.GET)
    List<Entity> list();

    @RequestMapping(value = "/b/save", method = RequestMethod.GET)
    int save();
}
