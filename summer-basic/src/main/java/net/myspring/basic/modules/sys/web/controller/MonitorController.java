package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sys/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

}
