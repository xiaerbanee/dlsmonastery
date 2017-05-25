package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.AccountWeixin;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.repository.AccountWeixinRepository;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountWeixinService {

    @Autowired
    private AccountWeixinRepository accountWeixinRepository;


    public AccountWeixinDto findOne(String id){
        return BeanUtil.map(accountWeixinRepository.findOne(id),AccountWeixinDto.class);
    }

    public AccountWeixinDto findByAccountId(String accountId) {
        AccountWeixin accountWeixinList =  accountWeixinRepository.findByAccountId(accountId);
        return BeanUtil.map(accountWeixinList,AccountWeixinDto.class);
    }

    public AccountWeixin save(AccountWeixin accountWeixin){
        accountWeixinRepository.save(accountWeixin);
        return accountWeixin;
    }
}
