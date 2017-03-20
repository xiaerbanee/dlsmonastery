package net.myspring.hr.web.controller;

import net.myspring.hr.domain.Account;
import net.myspring.hr.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/3/19.
 */
@RestController
@RequestMapping(value = "account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "findOne")
    public Account report(String id) {
        return accountService.findOne(id);
    }
}
