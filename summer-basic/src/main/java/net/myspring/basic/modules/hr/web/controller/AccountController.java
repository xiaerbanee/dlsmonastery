package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
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

    @RequestMapping(value = "save")
    public Account save() {
        return accountService.save();
    }

}
