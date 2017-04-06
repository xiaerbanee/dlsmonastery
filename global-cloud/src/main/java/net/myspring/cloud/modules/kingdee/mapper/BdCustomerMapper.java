package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface BdCustomerMapper {

    List<BdCustomer> findAll(@Param("maxOutDate")LocalDateTime maxOutDate);

    List<BdCustomer> findByName(@Param("name") String name);
}
