package net.myspring.hr.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.hr.modules.sys.service.TownService;

@Controller
@RequestMapping(value = "sys/town")
public class TownController {

    @Autowired
    private TownService townService;

}
