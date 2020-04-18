package top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.dao.AMapper;
import top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.service.AService;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.client.BasicServiceBClient;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@Service
public class AServiceImpl implements AService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AMapper aMapper;
    @Autowired
    private BasicServiceBClient serviceBClient;

    @Override
    public List<Entity> list() {
        return aMapper.findAll();
    }

    @Override
    @Transactional
    @LcnTransaction
    public int save() {
        int ret1 = aMapper.save("Arvin");
        int ret2 = aMapper.save("Lee");

        serviceBClient.save();

        return ret1+ret2;
    }
}
