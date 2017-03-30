package net.myspring.hr.modules.hr.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.hr.domain.EmployeeSalaryBasic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeSalaryBasicMapper extends MyMapper<EmployeeSalaryBasic,String> {

}
