package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface BdCustomerMapper {

    List<BdCustomer> findAll();

    List<BdCustomer> findByDate(@Param("maxOutDate")LocalDateTime maxOutDate);

    List<BdCustomer> findByName(@Param("name") String name);

    List<String> findName();

    BdCustomer findById(@Param("id")String id);

    List<BdCustomer> findByPrimaryGroup(@Param("primaryGroup")String primaryGroup);
}
