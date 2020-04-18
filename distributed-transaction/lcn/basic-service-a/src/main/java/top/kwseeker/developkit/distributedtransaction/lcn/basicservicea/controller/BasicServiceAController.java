package top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.service.AService;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@RestController
@RequestMapping("/a")
public class BasicServiceAController {

    @Autowired
    private AService aService;

    @RequestMapping("/list")
    public List<Entity> list(){
        return aService.list();
    }

    @RequestMapping("/save")
    public int save(){
        return aService.save();
    }
}
