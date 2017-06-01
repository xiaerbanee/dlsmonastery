package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.repository.EmployeeRepository;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.basic.modules.sys.manager.OfficeManager;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeManager officeManager;


    public EmployeeDto findOne(String id){
        Employee employee = employeeRepository.findOne(id);
        EmployeeDto employeeDto = BeanUtil.map(employee,EmployeeDto.class);
        return employeeDto;
    }

    public EmployeeDto findOne(EmployeeDto employeeDto){
        if(!employeeDto.isCreate()){
            Employee employee=employeeRepository.findOne(employeeDto.getId());
            employeeDto= BeanUtil.map(employee,EmployeeDto.class);
            cacheUtils.initCacheInput(employeeDto);
        }
        return employeeDto;
    }

    public Page<EmployeeDto> findPage(Pageable pageable, EmployeeQuery employeeQuery){
        employeeQuery.setOfficeIds(officeManager.officeFilter(RequestUtils.getAccountId()));
        Page<EmployeeDto> page=employeeRepository.findPage(pageable,employeeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<EmployeeDto> findByNameLike(String name){
        List<Employee> employeeList=employeeRepository.findByEnabledIsTrueAndNameContaining(name);
        List<EmployeeDto> employeeDtoList= BeanUtil.map(employeeList,EmployeeDto.class);
        cacheUtils.initCacheInput(employeeDtoList);
        return employeeDtoList;
    }

    public Employee save(EmployeeForm employeeForm) {
        Employee employee;
        if(employeeForm.isCreate()) {
            employee=BeanUtil.map(employeeForm,Employee.class);
            employeeRepository.save(employee);
        } else {
            employee = employeeRepository.findOne(employeeForm.getId());
            ReflectionUtil.copyProperties(employeeForm,employee);
            employeeRepository.save(employee);
        }
        return employee;
    }

    public void logicDelete(String id) {
        employeeRepository.logicDelete(id);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> findByIds(List<String> ids){
        if(CollectionUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<Employee> employees = employeeRepository.findAll(ids);
        List<EmployeeDto> employeeDto= BeanUtil.map(employees,EmployeeDto.class);
        return employeeDto;
    }
}
