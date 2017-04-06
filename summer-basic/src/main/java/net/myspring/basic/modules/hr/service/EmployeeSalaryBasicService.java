package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.mapper.EmployeeSalaryBasicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSalaryBasicService {

    @Autowired
    private EmployeeSalaryBasicMapper employeeSalaryBasicMapper;

}
