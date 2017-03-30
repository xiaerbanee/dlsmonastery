package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.EmployeeSalaryMapper;

@Service
public class EmployeeSalaryService {

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

}
