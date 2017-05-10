package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdDepartment;
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

    String findNameByDepartmentId(@Param("departmentId")String departmentId);

    List<NameNumberDto> findNameAndNumber();

    List<NameNumberDto> findByNameLike(@Param("name")String name);

}
