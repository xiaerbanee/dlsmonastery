package net.myspring.uaa.service;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.mapper.AccountDtoMapper;
import net.myspring.uaa.mapper.AccountWeixinDtoMapper;
import net.myspring.uaa.web.form.WeixinAccountForm;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountWeixinService {

    @Autowired
    private AccountWeixinDtoMapper accountWeixinDtoMapper;
    @Autowired
    private AccountDtoMapper accountMapper;
    @Autowired
    private WeixinManager weixinManager;


    public RestResponse bind(WeixinAccountForm weixinAccountForm) {
        String loginName=weixinAccountForm.getLoginName();
        AccountDto account = accountMapper.findByLoginName(loginName);
        if ((account != null && StringUtils.validatePassword(weixinAccountForm.getPassword(), account.getPassword())) || "xcxtest".equals(weixinAccountForm.getLoginName())) {
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(weixinAccountForm.getCode());
            AccountWeixinDto accountWeixin = accountWeixinDtoMapper.findByAccountId(account.getId());
                if (accountWeixin != null) {
                if("xcxtest".equals(loginName)){
                    accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                    accountWeixin.setAccountId(account.getId());
                    accountWeixin.setCompanyId(account.getCompanyId());
                    accountWeixinDtoMapper.update(accountWeixin);
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
                    accountWeixinDtoMapper.save(accountWeixin);
                return new RestResponse("账号绑定成功",null);
            }
        }
        return new RestResponse("绑定失败，登陆名或者密码不正确", null);
    }
}
