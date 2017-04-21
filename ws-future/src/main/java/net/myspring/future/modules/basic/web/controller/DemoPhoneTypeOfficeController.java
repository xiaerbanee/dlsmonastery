package net.myspring.future.modules.basic.web.controller;

import net.myspring.future.modules.basic.service.DemoPhoneTypeOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "basic/demoPhoneTypeOffice")
public class DemoPhoneTypeOfficeController {

    @Autowired
    private DemoPhoneTypeOfficeService demoPhoneTypeOfficeService;

}
