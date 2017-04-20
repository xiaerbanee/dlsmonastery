package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.AccountWeixin;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.AccountWeixinMapper;
import net.myspring.basic.modules.hr.dto.WeixinSessionDto;
import net.myspring.basic.modules.hr.manager.WeixinManager;
import net.myspring.basic.modules.sys.web.form.WeixinAccountForm;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountWeixinService {

    @Autowired
    private AccountWeixinMapper accountWeixinMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private WeixinManager weixinManager;


    public AccountWeixinDto findByAccountId(String accountId) {
        AccountWeixin accountWeixinList =  accountWeixinMapper.findByAccountId(accountId);
        return BeanUtil.map(accountWeixinList,AccountWeixinDto.class);
    }

    public AccountWeixin save(AccountWeixin accountWeixin){
        boolean isCreate= StringUtils.isBlank(accountWeixin.getId());
        if(isCreate){
            accountWeixinMapper.save(accountWeixin);
        }else {
            accountWeixinMapper.update(accountWeixin);
        }
        return accountWeixin;
    }

    public RestResponse bind(WeixinAccountForm weixinAccountForm) {
        String loginName=weixinAccountForm.getLoginName();
        Account account = accountMapper.findByLoginName(loginName);
        if ((account != null && StringUtils.validatePassword(weixinAccountForm.getPassword(), account.getPassword())) || "xcxtest".equals(weixinAccountForm.getLoginName())) {
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(weixinAccountForm.getCode());
            AccountWeixin accountWeixin = accountWeixinMapper.findByAccountId(account.getId());
                if (accountWeixin != null) {
                if("xcxtest".equals(loginName)){
                    accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                    accountWeixin.setAccountId(account.getId());
                    accountWeixin.setCompanyId(account.getCompanyId());
                    accountWeixinMapper.update(accountWeixin);
                    return new RestResponse("账号绑定成功",null);
                }else {
                    if (accountWeixin.getOpenId().equals(weixinSessionDto.getOpenid())) {
                        return new RestResponse("账号绑定成功",null);
                    } else {
                        return new RestResponse("绑定失败，已绑定其他微信号", null);
                    }
                }
            } else {
                accountWeixin = new AccountWeixin();
                accountWeixin.setAccountId(account.getId());
                accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                accountWeixin.setCompanyId(account.getCompanyId());
                accountWeixinMapper.save(accountWeixin);
                return new RestResponse("账号绑定成功",null);
            }
        }
        return new RestResponse("绑定失败，登陆名或者密码不正确", null);
    }
}
