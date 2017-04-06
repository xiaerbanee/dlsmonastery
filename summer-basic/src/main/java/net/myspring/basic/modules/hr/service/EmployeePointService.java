package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.mapper.EmployeePointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeePointService {

    @Autowired
    private EmployeePointMapper employeePointMapper;
    @Autowired
    private EmployeeMapper employeeMapper;


}
