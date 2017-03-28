package net.myspring.future.report.web.controller;

import net.myspring.future.report.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/3/28.
 */
@RestController
@RequestMapping(value = "account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "findOne")
    public String report(String id) {
        return accountService.findOne(id);
    }

}
