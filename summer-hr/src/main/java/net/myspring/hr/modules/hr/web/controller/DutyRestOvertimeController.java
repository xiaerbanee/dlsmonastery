package net.myspring.hr.modules.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.hr.modules.hr.service.DutyRestOvertimeService;

@Controller
@RequestMapping(value = "hr/dutyRestOvertime")
public class DutyRestOvertimeController {

    @Autowired
    private DutyRestOvertimeService dutyRestOvertimeService;

}
