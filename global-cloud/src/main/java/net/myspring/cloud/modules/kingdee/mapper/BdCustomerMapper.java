package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/5/11.
 */
@Mapper
public interface BdCustomerMapper {

    List<BdCustomer> findByNameList(@Param("nameList")List<String> nameList);

}
