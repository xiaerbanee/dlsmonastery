package net.myspring.uaa.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.service.AccountWeixinService;
import net.myspring.uaa.web.form.AccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhucc on 2017/4/12.
 */
@RestController
@RequestMapping(value = "sys/accountWeixin")
public class AccountWeixinController {

    @Autowired
    private AccountWeixinService accountWeixinService;

    @RequestMapping(value = "bind")
    public RestResponse accountBind(AccountForm accountForm) {
        return accountWeixinService.bind(accountForm);
    }
}
