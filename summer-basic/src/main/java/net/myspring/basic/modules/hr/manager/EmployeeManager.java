package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2017/4/5.
 */
@Component
@CacheConfig(cacheNames = "employees")
public class EmployeeManager {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Cacheable(key="#p0")
    public Employee findOne(String id) {
        return employeeMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Employee save(Employee employee){
        employeeMapper.save(employee);
        return  employee;
    }

    @CachePut(key="#p0.id")
    public Employee update(Employee employee){
        employeeMapper.update(employee);
        return  employeeMapper.findOne(employee.getId());
    }

    @CachePut(key="#p0.id")
    public Employee updateForm(EmployeeForm employeeForm){
        employeeMapper.updateForm(employeeForm);
        return  employeeMapper.findOne(employeeForm.getId());
    }

    @CachePut(key="#p0")
    public Employee updateAccountId(String employeeId,String accountId){
        employeeMapper.updateAccountId(employeeId,accountId);
        return  employeeMapper.findOne(employeeId);
    }
}
