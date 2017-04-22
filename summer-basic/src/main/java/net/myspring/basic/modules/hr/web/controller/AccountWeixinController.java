package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
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

    @RequestMapping(value = "findByAccountId")
    public AccountWeixinDto findByAccountId(String accountId) {
        return accountWeixinService.findByAccountId(accountId);
    }
}
