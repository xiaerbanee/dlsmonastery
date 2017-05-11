package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.input.dto.NameNumberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/5/11.
 */
@Mapper
public interface BdDepartmentMapper {

    List<NameNumberDto> findCustomerNameAndDepartNumberByCustomerNameList(@Param("customerNameList") List<String> customerNameList);

}
