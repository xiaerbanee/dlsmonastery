package net.myspring.basic.modules.hr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.SalaryTemplateDetailService;

@Controller
@RequestMapping(value = "hr/salaryTemplateDetail")
public class SalaryTemplateDetailController {

    @Autowired
    private SalaryTemplateDetailService salaryTemplateDetailService;

}
