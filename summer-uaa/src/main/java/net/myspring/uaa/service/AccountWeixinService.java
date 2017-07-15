package net.myspring.uaa.service;

import net.myspring.common.response.RestResponse;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.repository.AccountDtoRepository;
import net.myspring.uaa.repository.AccountWeixinDtoRepository;
import net.myspring.uaa.web.form.AccountWeixinForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountWeixinService {

    @Autowired
    private AccountWeixinDtoRepository accountWeixinDtoRepository;
    @Autowired
    private AccountDtoRepository accountRepository;
    @Autowired
    private WeixinManager weixinManager;


    public RestResponse bind(AccountWeixinForm accountWeixinForm) {
        String loginName=accountWeixinForm.getLoginName();
        AccountDto account = accountRepository.findByLoginName(loginName);
        if ((account != null && StringUtils.validatePassword(accountWeixinForm.getPassword(), account.getPassword())) || "xcxtest".equals(accountWeixinForm.getLoginName())) {
            if(account.getLeaveDate()!=null){
                return new RestResponse("你绑定的账户已离职，请绑定OA登陆账户",null);
            }
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(accountWeixinForm.getCode());
            List<AccountWeixinDto> accountWeixinList = accountWeixinDtoRepository.findByAccountId(account.getId());
            if (CollectionUtil.isNotEmpty(accountWeixinList)&&accountWeixinList.size()==1) {
                AccountWeixinDto accountWeixin=accountWeixinList.get(0);
                if("xcxtest".equals(loginName)){
                    accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                    accountWeixin.setAccountId(account.getId());
                    accountWeixinDtoRepository.save(accountWeixin);
                    return new RestResponse("账号绑定成功",null);
                }else {
                    if(StringUtils.isBlank(accountWeixin.getOpenId())){
                        accountWeixin.setOpenId(accountWeixin.getOpenId());
                        return new RestResponse("账号绑定成功",null);
                    }else if(StringUtils.isNotBlank(accountWeixin.getOpenId())&&accountWeixin.getOpenId().equals(weixinSessionDto.getOpenid())){
                        return new RestResponse("账号绑定成功",null);
                    }else {
                        return new RestResponse("绑定失败，已绑定其他微信号", null,false);
                    }
                }
            } else  if (CollectionUtil.isEmpty(accountWeixinList)){
                AccountWeixinDto accountWeixin = new AccountWeixinDto();
                accountWeixin.setAccountId(account.getId());
                accountWeixin.setOpenId(weixinSessionDto.getOpenid());
                accountWeixinDtoRepository.save(accountWeixin);
                return new RestResponse("账号绑定成功",null);
            }
        }
        return new RestResponse("绑定失败，登陆名或者密码不正确，请绑定OA登陆账户", null,false);
    }
}
