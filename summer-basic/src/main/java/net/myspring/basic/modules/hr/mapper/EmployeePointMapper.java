package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.EmployeePoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeePointMapper extends MyMapper<EmployeePoint,String> {

}
