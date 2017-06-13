package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BasAssistant;
import net.myspring.cloud.modules.kingdee.service.BasAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 辅助资料
 * Created by lihx on 2017/6/13.
 */
@RestController
@RequestMapping(value = "kingdee/basAssistant")
public class BasAssistantController {
    @Autowired
    private BasAssistantService basAssistantService;

    @RequestMapping(value = "findByName")
    public BasAssistant findByName(String name){
        return basAssistantService.findByName(name);
    }
}
