package net.myspring.hr.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.hr.modules.sys.service.DictMapService;

@Controller
@RequestMapping(value = "sys/dictMap")
public class DictMapController {

    @Autowired
    private DictMapService dictMapService;

}
