package net.myspring.basic.modules.hr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.DutyAnnualService;

@Controller
@RequestMapping(value = "hr/dutyAnnual")
public class DutyAnnualController {

    @Autowired
    private DutyAnnualService dutyAnnualService;

}
