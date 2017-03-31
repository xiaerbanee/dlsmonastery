package net.myspring.hr.modules.hr.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.hr.domain.EmployeePoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeePointMapper extends MyMapper<EmployeePoint,String> {

}