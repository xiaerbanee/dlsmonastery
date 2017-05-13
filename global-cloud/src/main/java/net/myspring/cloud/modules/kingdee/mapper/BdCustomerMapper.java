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

    List<BdCustomer> findByIdList(@Param("idList")List<String> idList);

    BdCustomer findById(@Param("id")String id);

    List<String> findNameByNameLike(@Param("name")String name);

    List<BdCustomer> findByNameLike(@Param("name")String name);

    BdCustomer findTopOne();

    List<BdCustomer> findByPrimaryGroup(@Param("primaryGroup")String primaryGroup);

    List<BdCustomer> findPrimaryGroupAndPrimaryGroupName();
}
