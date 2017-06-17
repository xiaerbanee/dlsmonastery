package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.service.BdAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/6/8.
 */
@RestController
@RequestMapping(value = "kingdee/bdAccount")
public class BdAccountController {
    @Autowired
    private BdAccountService bdAccountService;

    @RequestMapping(value = "findByNumber")
    public BdAccount findByNumber(String number){
        BdAccount bdAccount = bdAccountService.findByNumber(number);
        return bdAccount;
    }

    @RequestMapping(value = "findByName")
    public BdAccount findByName(String name){
        BdAccount bdAccount = bdAccountService.findByName(name);
        return bdAccount;
    }
}
