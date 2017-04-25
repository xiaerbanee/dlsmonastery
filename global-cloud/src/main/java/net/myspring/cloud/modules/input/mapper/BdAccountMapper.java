package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface BdAccountMapper {

    String findFNumberByName(@Param("name") String name);

    List<BdAccount> findAllSubject();
}
