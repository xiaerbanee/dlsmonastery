package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper extends MyMapper<Employee,String> {

    Page<EmployeeDto> findPage(Pageable pageable, @Param("p")EmployeeQuery employeeQuery);

    List<Employee> findByNameLike(String name);

    List<Employee> findByStatusAndregularDate(@Param("status") String status,@Param("regularDate") LocalDateTime regularDate);

    int updateAccountId(@Param("employeeId")String employeeId,@Param("accountId")String accountId);

}
