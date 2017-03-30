package net.myspring.hr.modules.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.hr.modules.hr.service.EmployeePointService;

@Controller
@RequestMapping(value = "hr/employeePoint")
public class EmployeePointController {

    @Autowired
    private EmployeePointService employeePointService;

}
