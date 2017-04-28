package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by liuj on 2017/3/19.
 */
@Component
@CacheConfig(cacheNames = "accounts")
public class AccountManager {
    @Autowired
    private AccountMapper accountMapper;

    @Cacheable(key="#p0")
    public Account findOne(String id) {
        return accountMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Account save(Account account){
        accountMapper.save(account);
        return  account;
    }

    @CachePut(key="#p0.id")
    public Account update(Account account){
        accountMapper.update(account);
        return  accountMapper.findOne(account.getId());
    }

    @CachePut(key="#p0.id")
    public Account updateForm(AccountForm accountForm){
        accountMapper.updateForm(accountForm);
        return  accountMapper.findOne(accountForm.getId());
    }
}
