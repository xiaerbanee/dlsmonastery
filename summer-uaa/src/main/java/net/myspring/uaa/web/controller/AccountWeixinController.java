package net.myspring.uaa.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.service.AccountWeixinService;
import net.myspring.uaa.web.form.AccountWeixinForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/3/19.
 */
@RestController
@RequestMapping(value = "accountWeixin")
public class AccountWeixinController {

    @Autowired
    private AccountWeixinService accountWeixinService;

    @RequestMapping(value = "bind")
    public RestResponse accountBind(AccountWeixinForm accountWeixinForm) {
        return accountWeixinService.bind(accountWeixinForm);
    }
}
