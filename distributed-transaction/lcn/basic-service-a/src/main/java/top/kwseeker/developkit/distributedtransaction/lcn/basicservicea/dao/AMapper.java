package top.kwseeker.developkit.distributedtransaction.lcn.basicservicea.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@Mapper
public interface AMapper {

    @Select("select * from t_a")
    List<Entity> findAll();

    @Options(useGeneratedKeys = true, keyProperty = "", keyColumn = "id")
    @Insert("insert into t_a(name) values(#{name})")
    int save(@Param("name") String name);
}
