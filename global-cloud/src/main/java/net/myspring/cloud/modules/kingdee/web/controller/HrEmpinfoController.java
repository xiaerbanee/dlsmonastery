package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.service.HrEmpinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/4/6.
 */
@RestController
@RequestMapping(value = "kingdee/hrEmpinfo")
public class HrEmpinfoController {
    @Autowired
    private HrEmpinfoService hrEmpinfoService;

    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public String getCode(String name) {
        return hrEmpinfoService.findFNumberByName(name);
    }
}
