package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeePointService {

    @Autowired
    private EmployeeMapper employeeMapper;


}
