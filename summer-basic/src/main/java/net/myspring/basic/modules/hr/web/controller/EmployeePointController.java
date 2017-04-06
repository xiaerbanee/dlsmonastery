package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.service.EmployeePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "hr/employeePoint")
public class EmployeePointController {

    @Autowired
    private EmployeePointService employeePointService;

}
