package net.myspring.basic.modules.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.AccountVerifyService;

@Controller
@RequestMapping(value = "hr/accountVerify")
public class AccountVerifyController {

    @Autowired
    private AccountVerifyService accountVerifyService;

}
