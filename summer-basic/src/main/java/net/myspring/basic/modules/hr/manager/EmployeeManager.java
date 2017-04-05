package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class EmployeeManager {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Cacheable(value = "employees",key="#p0")
    public Employee findOne(String id) {
        return employeeMapper.findOne(id);
    }

    @CachePut(value = "employees",key="#p0.id")
    public Employee save(Employee employee){
        employeeMapper.save(employee);
        return  employee;
    }

    @CachePut(value = "employees",key="#p0.id")
    public Employee update(Employee employee){
        employeeMapper.update(employee);
        return  employeeMapper.findOne(employee.getId());
    }
    
    public int updateAccountId(String employeeId,String accountId){
        return employeeMapper.updateAccountId(employeeId,accountId);
    }

}

