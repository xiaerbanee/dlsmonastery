package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * 客户
 * Created by lihx on 2017/5/11.
 */
@Mapper
public interface BdCustomerMapper {

    List<BdCustomer> findByNameList(@Param("nameList")List<String> nameList);

    List<String> findNameByNameLike(@Param("name")String name);

}
