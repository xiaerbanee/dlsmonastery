package net.myspring.hr.manager;

import net.myspring.hr.domain.Account;
import net.myspring.hr.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by liuj on 2017/3/19.
 */
@Component
public class AccountManager {

    @Autowired
    private AccountMapper accountMapper;

    @Cacheable(value = "account",key="#p0")
    public Account findOne(String id) {
        Account account = new Account();
        account.setId(id);
        account.setName(id);
        return account;
    }
    
}
