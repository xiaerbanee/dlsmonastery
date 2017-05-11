package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface BdDepartmentMapper {

    BdDepartment findByCustomerId(@Param("customerId")String customerId);

    List<BdDepartment> findAll();

    BdDepartment findByName(String name);

    List<BdDepartment> findByNameLike(@Param("name")String name);

}
