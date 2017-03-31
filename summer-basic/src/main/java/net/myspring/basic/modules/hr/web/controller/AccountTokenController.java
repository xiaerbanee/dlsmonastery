package net.myspring.basic.modules.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.AccountTokenService;

@Controller
@RequestMapping(value = "hr/accountToken")
public class AccountTokenController {

    @Autowired
    private AccountTokenService accountTokenService;

}
