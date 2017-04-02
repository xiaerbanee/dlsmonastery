package net.myspring.basic.modules.hr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.DutyLeaveService;

@Controller
@RequestMapping(value = "hr/dutyLeave")
public class DutyLeaveController {

    @Autowired
    private DutyLeaveService dutyLeaveService;

}
