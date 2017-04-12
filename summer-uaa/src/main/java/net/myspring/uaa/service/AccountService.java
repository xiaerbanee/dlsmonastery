package net.myspring.uaa.service;

import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.mapper.AccountDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhucc on 2017/4/12.
 */
@Service
public class AccountService {
    @Autowired
    private AccountDtoMapper accountDtoMapper;

    public AccountDto findByLoginName(String loginName){
        AccountDto accountDto=accountDtoMapper.findByLoginName(loginName);
        return accountDto;
    }
}
