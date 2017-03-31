package net.myspring.basic.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.sys.service.DistrictService;

@Controller
@RequestMapping(value = "sys/district")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

}
