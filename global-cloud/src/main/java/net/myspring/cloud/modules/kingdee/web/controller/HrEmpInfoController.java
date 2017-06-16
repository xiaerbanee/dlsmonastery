package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.HrEmpInfo;
import net.myspring.cloud.modules.kingdee.service.HrEmpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by lihx on 2017/6/16.
 */
@RestController
@RequestMapping(value = "kingdee/hrEmpInfo")
public class HrEmpInfoController {
    @Autowired
    private HrEmpInfoService hrEmpInfoService;


    @RequestMapping(value = "findByName")
    public HrEmpInfo findByName(String name){
        return hrEmpInfoService.findByName(name);
    }
}
