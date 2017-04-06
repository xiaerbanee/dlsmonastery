package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.service.BasAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/4/6.
 */
@RestController
@RequestMapping(value = "kingdee/basAssistant")
public class BasAssistantController {
    @Autowired
    private BasAssistantService basAssistantService;

    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public String getCode(String lbName, String name) {
        return basAssistantService.findFNumberByNameAndDataValue(lbName,name);
    }
}
