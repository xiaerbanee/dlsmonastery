package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.AccountWeixin;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.mapper.AccountWeixinMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountWeixinService {

    @Autowired
    private AccountWeixinMapper accountWeixinMapper;


    public AccountWeixinDto findOne(String id){
        return BeanUtil.map(accountWeixinMapper.findOne(id),AccountWeixinDto.class);
    }

    public List<AccountWeixinDto> findByAccountId(String accountId) {
        List<AccountWeixin> accountWeixinList =  accountWeixinMapper.findByAccountId(accountId);
        return BeanUtil.map(accountWeixinList,AccountWeixinDto.class);
    }

}
