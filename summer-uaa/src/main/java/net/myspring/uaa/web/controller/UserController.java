package net.myspring.uaa.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.service.AccountWeixinService;
import net.myspring.uaa.service.UserService;
import net.myspring.uaa.web.form.AccountWeixinForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    FindByIndexNameSessionRepository<? extends ExpiringSession> sessions;

    @Autowired
    private UserService userService;
    @Autowired
    private AccountWeixinService accountWeixinService;

    @RequestMapping(value = "/user/logout")
    @ResponseBody
    public Boolean logout(Principal principal) {
        Collection<? extends ExpiringSession> usersSessions = this.sessions
                .findByIndexNameAndIndexValue(
                        FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                        principal.getName())
                .values();
        for (ExpiringSession expiringSession : usersSessions) {
            sessions.delete(expiringSession.getId());
        }
        return true;
    }


    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/login")
    public String loginPage(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        // some other stuff
        return "login";
    }

    //模拟UAA登陆，不需要跳转页面
    @RequestMapping(value = "/user/login")
    @ResponseBody
    public Map<String, Object> login(String username, String password, String weixinCode,String accountId) {
        Map<String, Object> map = userService.login(username, password, weixinCode,accountId);
        return map;
    }

    @RequestMapping(value = "/user/bind")
    @ResponseBody
    public RestResponse accountBind(AccountWeixinForm accountWeixinForm) {
        return accountWeixinService.bind(accountWeixinForm);
    }
}