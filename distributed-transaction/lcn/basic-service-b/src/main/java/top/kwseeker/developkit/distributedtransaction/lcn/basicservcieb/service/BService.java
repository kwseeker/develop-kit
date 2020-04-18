package top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.service;

import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

public interface BService {

    List<Entity> list();
    int save();
}
