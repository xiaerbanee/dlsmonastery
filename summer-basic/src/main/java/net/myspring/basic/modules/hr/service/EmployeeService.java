package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.basic.modules.sys.manager.OfficeManager;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeManager officeManager;


    public Employee findOne(String id){
        Employee employee = employeeMapper.findOne(id);
        return employee;
    }

    public EmployeeForm findForm(EmployeeForm employeeForm){
        if(!employeeForm.isCreate()){
            Employee employee=employeeMapper.findOne(employeeForm.getId());
            employeeForm= BeanUtil.map(employee,EmployeeForm.class);
            cacheUtils.initCacheInput(employeeForm);
        }
        return employeeForm;
    }

    public Page<EmployeeDto> findPage(Pageable pageable, EmployeeQuery employeeQuery){
        employeeQuery.setOfficeIds(officeManager.officeFilter(SecurityUtils.getAccountId()));
        Page<EmployeeDto> page=employeeMapper.findPage(pageable,employeeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<EmployeeDto> findByNameLike(String name){
        List<Employee> employeeList=employeeMapper.findByNameLike(name);
        List<EmployeeDto> employeeDtoList= BeanUtil.map(employeeList,EmployeeDto.class);
        cacheUtils.initCacheInput(employeeDtoList);
        return employeeDtoList;
    }

    public Employee save(EmployeeForm employeeForm) {
        Employee employee;
        if(employeeForm.isCreate()) {
            employee=BeanUtil.map(employeeForm,Employee.class);
            employeeMapper.save(employee);
        } else {
            employee = employeeMapper.findOne(employeeForm.getId());
            ReflectionUtil.copyProperties(employeeForm,employee);
            employeeMapper.update(employee);
        }
        return employee;
    }

    public void logicDeleteOne(String id) {
        employeeMapper.logicDeleteOne(id);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> findById(String id){
        List<Employee> employees = employeeMapper.findById(id);
        List<EmployeeDto> employeeDto= BeanUtil.map(employees,EmployeeDto.class);
        return employeeDto;
    }
}
