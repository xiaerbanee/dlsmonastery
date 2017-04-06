package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.mapper.EmployeeSalaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSalaryService {

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

}
