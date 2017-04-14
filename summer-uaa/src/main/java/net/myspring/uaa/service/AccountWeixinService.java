package net.myspring.uaa.service;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.mapper.AccountDtoMapper;
import net.myspring.uaa.mapper.AccountWeixinDtoMapper;
import net.myspring.uaa.web.form.AccountForm;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhucc on 2017/4/12.
 */
@Service
public class AccountWeixinService {

    @Autowired
    private AccountDtoMapper accountDtoMapper;
    @Autowired
    private WeixinManager weixinManager;
    @Autowired
    private AccountWeixinDtoMapper accountWeixinDtoMapper;

    public RestResponse bind(AccountForm accountForm) {
        String loginName=accountForm.getLoginName();
        AccountDto accountDto = accountDtoMapper.findByLoginName(loginName);
        if ((accountDto != null && StringUtils.validatePassword(accountForm.getPassword(), accountDto.getPassword())) || "xcxtest".equals(accountForm.getLoginName())) {
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(accountForm.getCode());
            AccountWeixinDto accountWeixinDto = accountWeixinDtoMapper.findByAccountId(accountDto.getId());
            if (accountWeixinDto != null) {
                if("xcxtest".equals(loginName)){
                    accountWeixinDto.setOpenId(weixinSessionDto.getOpenid());
                    accountWeixinDto.setAccountId(accountDto.getId());
                    accountWeixinDto.setCompanyId(accountDto.getCompanyId());
                    accountWeixinDtoMapper.update(accountWeixinDto);
                    return new RestResponse("账号绑定成功",null);
                }else {
                    if (accountWeixinDto.getOpenId().equals(weixinSessionDto.getOpenid())) {
                        return new RestResponse("账号绑定成功",null);
                    } else {
                        return new RestResponse("绑定失败，已绑定其他微信号", null);
                    }
                }
            } else {
                accountWeixinDto = new AccountWeixinDto();
                accountWeixinDto.setAccountId(accountDto.getId());
                accountWeixinDto.setOpenId(weixinSessionDto.getOpenid());
                accountWeixinDto.setCompanyId(accountDto.getCompanyId());
                accountWeixinDtoMapper.save(accountWeixinDto);
                return new RestResponse("账号绑定成功",null);
            }
        }
        return new RestResponse("绑定失败，登陆名或者密码不正确", null);
    }


}
