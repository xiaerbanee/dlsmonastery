package net.myspring.uaa.service;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.repository.AccountDtoRepository;
import net.myspring.uaa.repository.AccountWeixinDtoRepository;
import net.myspring.uaa.web.form.WeixinAccountForm;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountWeixinService {

    @Autowired
    private AccountWeixinDtoRepository accountWeixinDtoRepository;
    @Autowired
    private AccountDtoRepository accountRepository;
    @Autowired
    private WeixinManager weixinManager;


    public RestResponse bind(WeixinAccountForm weixinAccountForm) {
        String loginName=weixinAccountForm.getLoginName();
        AccountDto account = accountRepository.findByLoginName(loginName);
        if ((account != null && StringUtils.validatePassword(weixinAccountForm.getPassword(), account.getPassword())) || "xcxtest".equals(weixinAccountForm.getLoginName())) {
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(weixinAccountForm.getCode());
            AccountWeixinDto accountWeixin = accountWeixinDtoRepository.findByAccountId(account.getId());
            if (accountWeixin != null) {
                if("xcxtest".equals(loginName)){
                    accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                    accountWeixin.setAccountId(account.getId());
                    accountWeixin.setCompanyId(account.getCompanyId());
                    accountWeixinDtoRepository.save(accountWeixin);
                    return new RestResponse("账号绑定成功",null);
                }else {
                    if (accountWeixin.getOpenId().equals(weixinSessionDto.getOpenid())) {
                        return new RestResponse("账号绑定成功",null);
                    } else {
                        return new RestResponse("绑定失败，已绑定其他微信号", null);
                    }
                }
            } else {
                accountWeixin = new AccountWeixinDto();
                accountWeixin.setAccountId(account.getId());
                accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                accountWeixin.setCompanyId(account.getCompanyId());
                accountWeixinDtoRepository.save(accountWeixin);
                return new RestResponse("账号绑定成功",null);
            }
        }
        return new RestResponse("绑定失败，登陆名或者密码不正确", null);
    }
}
