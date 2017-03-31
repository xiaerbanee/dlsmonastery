package net.myspring.basic.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.hr.mapper.EmployeeSalaryMapper;

@Service
public class EmployeeSalaryService {

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

}
