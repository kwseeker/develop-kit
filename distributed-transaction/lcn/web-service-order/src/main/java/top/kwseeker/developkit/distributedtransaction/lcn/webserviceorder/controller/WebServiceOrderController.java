package top.kwseeker.developkit.distributedtransaction.lcn.webserviceorder.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.client.BasicServiceAClient;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.client.BasicServiceBClient;

@RestController
@RequestMapping("/web")
public class WebServiceOrderController {

    @Autowired
    BasicServiceAClient serviceAClient;
    @Autowired
    BasicServiceBClient serviceBClient;

    @RequestMapping("/order")
    //不起作用（TODO:?）
    @Transactional
    @LcnTransaction()
    public String order() {
        serviceAClient.save();
        serviceBClient.save();
        return "done";
    }
}
