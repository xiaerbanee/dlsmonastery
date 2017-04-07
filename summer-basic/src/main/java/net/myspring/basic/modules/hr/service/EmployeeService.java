package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.manager.EmployeeManager;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.util.mapper.BeanMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public Employee findOne(String id){
        Employee employee=employeeManager.findOne(id);
        return employee;
    }

    public EmployeeDto findDto(String id){
        Employee employee=employeeManager.findOne(id);
        EmployeeDto employeeDto=BeanMapper.convertDto(employee,EmployeeDto.class);
        cacheUtils.initCacheInput(employeeDto);
        return employeeDto;
    }

    public Page<EmployeeDto> findPage(Pageable pageable, EmployeeQuery employeeQuery){
        Page<Employee> page=employeeMapper.findPage(pageable,employeeQuery);
        Page<EmployeeDto> employeeDtoPage=BeanMapper.convertPage(page,EmployeeDto.class);
        cacheUtils.initCacheInput(employeeDtoPage.getContent());
        return employeeDtoPage;
    }

    public List<EmployeeDto> findByNameLike(String name){
        List<Employee> employeeList=employeeMapper.findByNameLike(name);
        List<EmployeeDto> employeeDtoList= BeanMapper.convertDtoList(employeeList,EmployeeDto.class);
        cacheUtils.initCacheInput(employeeDtoList);
        return employeeDtoList;
    }

    public EmployeeForm save(EmployeeForm employeeForm) {
        boolean isCreate= StringUtils.isBlank(employeeForm.getId());
        if(isCreate) {
            employeeManager.saveForm(employeeForm);
        } else {
            employeeManager.updateForm(employeeForm);
        }
        return employeeForm;
    }

    public void logicDeleteOne(String id) {
        employeeMapper.logicDeleteOne(id);
    }
}
