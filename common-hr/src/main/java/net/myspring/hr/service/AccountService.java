package net.myspring.hr.service;

import net.myspring.hr.domain.Account;
import net.myspring.hr.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/3/19.
 */
@Service
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;

    public Account findOne(String id) {
        return accountMapper.findOne(id);
    }
}
