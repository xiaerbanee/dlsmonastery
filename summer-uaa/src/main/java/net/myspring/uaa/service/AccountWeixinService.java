package net.myspring.uaa.service;

import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.mapper.AccountWeixinDtoMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhucc on 2017/4/12.
 */
@Service
public class AccountWeixinService {

    @Autowired
    private WeixinManager weixinManager;
    @Autowired
    private AccountWeixinDtoMapper accountWeixinDtoMapper;

    public WeixinSessionDto findWeixinSessionDto(String code){
        return weixinManager.findWeixinSessionDto(code);
    }

    public AccountWeixinDto findByAccountId(String accountId){
        return accountWeixinDtoMapper.findByAccountId(accountId);
    }

    public AccountWeixinDto save(AccountWeixinDto accountWeixinDto){
        accountWeixinDtoMapper.saveDto(accountWeixinDto);
        return accountWeixinDto;
    }

    public AccountWeixinDto update(AccountWeixinDto accountWeixinDto){
        accountWeixinDtoMapper.updateDto(accountWeixinDto);
        return accountWeixinDto;
    }


}
