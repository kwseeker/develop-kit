package top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.service.BService;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@RestController
@RequestMapping("/b")
public class BasicServiceBController {

    @Autowired
    private BService bService;

    @RequestMapping("/list")
    public List<Entity> list() {
        return bService.list();
    }

    @RequestMapping("/save")
    public int save() {
        return bService.save();
    }
}
