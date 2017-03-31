package net.myspring.basic.modules.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.DutySignService;

@Controller
@RequestMapping(value = "hr/dutySign")
public class DutySignController {

    @Autowired
    private DutySignService dutySignService;

}
