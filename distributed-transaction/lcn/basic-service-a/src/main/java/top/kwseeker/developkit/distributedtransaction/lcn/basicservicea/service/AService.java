package top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.service;

import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

public interface AService {

    List<Entity> list();

    int save();
}
