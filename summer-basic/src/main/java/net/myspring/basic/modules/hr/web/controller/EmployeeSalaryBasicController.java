package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.service.EmployeeSalaryBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hr/employeeSalaryBasic")
public class EmployeeSalaryBasicController {

    @Autowired
    private EmployeeSalaryBasicService employeeSalaryBasicService;

}
