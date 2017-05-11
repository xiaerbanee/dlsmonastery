package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
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

    List<NameNumberDto> findPrimaryGroupAndPrimaryGroupName();

    List<BdCustomer> findByDate(@Param("maxOutDate")LocalDateTime maxOutDate);

    List<BdCustomer> findByName(@Param("name") String name);

    List<String> findNameByNameLike(@Param("name") String name);

    BdCustomer findById(@Param("id")String id);

    List<BdCustomer> findByPrimaryGroup(@Param("primaryGroup")String primaryGroup);

    String findNumberByName(@Param("name")String name);

    List<NameNumberDto> findNameAndNumber();
}
