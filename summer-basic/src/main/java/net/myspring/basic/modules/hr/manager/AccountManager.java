package net.myspring.basic.modules.hr.manager;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
