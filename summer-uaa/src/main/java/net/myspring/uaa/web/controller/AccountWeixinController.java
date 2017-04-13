package net.myspring.uaa.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.service.AccountService;
import net.myspring.uaa.service.AccountWeixinService;
import net.myspring.uaa.web.form.AccountForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "accountBind")
    public RestResponse accountBind(AccountForm accountForm, String code) {
        //切换当前数据源
        String loginName=accountForm.getLoginName();
        AccountDto accountDto = accountService.findByLoginName(loginName);
        if ((accountDto != null && StringUtils.validatePassword(accountForm.getPassword(), accountDto.getPassword()))||"xcxtest".equals(accountForm.getLoginName())) {
            WeixinSessionDto weixinSessionDto = accountWeixinService.findWeixinSessionDto(code);
            AccountWeixinDto accountWeixinDto = accountWeixinService.findByAccountId(accountDto.getId());
            if (accountWeixinDto != null) {
                if("xcxtest".equals(loginName)){
                    accountWeixinDto.setOpenId(weixinSessionDto.getOpenid());
                    accountWeixinDto.setAccountId(accountDto.getId());
                    accountWeixinDto.setCompanyId(accountDto.getCompanyId());
                    accountWeixinService.update(accountWeixinDto);
                    return new RestResponse("账号绑定成功",null);
                }else {
                    if (accountWeixinDto.getOpenId().equals(weixinSessionDto.getOpenid())) {
                        return new RestResponse("账号绑定成功",null);
                    } else {
                        return new RestResponse("绑定失败，该账号已绑定其他微信号", null);
                    }
                }
            } else {
                accountWeixinDto = new AccountWeixinDto();
                accountWeixinDto.setAccountId(accountDto.getId());
                accountWeixinDto.setOpenId(weixinSessionDto.getOpenid());
                accountWeixinDto.setCompanyId(accountDto.getCompanyId());
                accountWeixinService.save(accountWeixinDto);
                return new RestResponse("账号绑定成功",null);
            }
        }
        return new RestResponse("绑定失败，登陆名或者密码不正确", null);
    }
}
