package top.kwseeker.developkit.distributedtransaction.lcn.basicservcieb.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kwseeker.developkit.distributedtransaction.lcn.servicecommon.entity.Entity;

import java.util.List;

@Repository
@Mapper
public interface BMapper {

    @Select("select * from t_b")
    List<Entity> findAll();

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into t_b(name) values(#{name})")
    int save(@Param("name") String name);
}
