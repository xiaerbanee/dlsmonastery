package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.service.BdMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/5/12.
 */
@RestController
@RequestMapping(value = "kingdee/bdMaterial")
public class BdMaterialController {
    @Autowired
    private BdMaterialService bdMaterialService;

    @RequestMapping(value = "findByName")
    public BdMaterial findByName(String name){
        return bdMaterialService.findByName(name);
    }

    @RequestMapping(value = "findByNumber")
    public BdMaterial findByNumber(String number){
        return bdMaterialService.findByNumber(number);
    }
}
