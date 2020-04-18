package top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.dao.BMapper;
import top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.service.BService;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@Service
public class BServiceImpl implements BService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BMapper bMapper;

    @Override
    public List<Entity> list() {
        return bMapper.findAll();
    }

    @Override
    @Transactional
    @LcnTransaction
    public int save() {
        int ret1 = bMapper.save("Jack");
        int i = 1/0;    //模拟运行时异常
        int ret2 = bMapper.save("Chen");
        return ret1+ret2;
    }
}
