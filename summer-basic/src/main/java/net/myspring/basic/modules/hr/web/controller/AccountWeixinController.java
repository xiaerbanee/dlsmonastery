package net.myspring.basic.modules.hr.web.controller;


import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.AccountWeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/3/19.
 */
@RestController
@RequestMapping(value = "hr/accountWeixin")
public class AccountWeixinController {

    @Autowired
    private AccountWeixinService accountWeixinService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "findOne")
    public AccountWeixinDto findOne(String id){
        AccountWeixinDto accountWeixinDto = accountWeixinService.findOne(id);
        return accountWeixinDto;
    }

    @RequestMapping(value = "findByAccountId")
    public AccountWeixinDto findByAccountId(String accountId){
        return accountWeixinService.findByAccountId(accountId);
    }
}
