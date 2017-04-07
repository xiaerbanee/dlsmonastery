package net.myspring.basic.modules.hr.manager;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */
@Component
public class AccountManager {

    @Autowired
    private AccountMapper accountMapper;

    @Cacheable(value = "accounts",key="#p0")
    public Account findOne(String id) {
        return accountMapper.findOne(id);
    }

    public Page<Account> findPage(Pageable pageable, AccountQuery accountQuery) {
        Page<Account> page = accountMapper.findPage(pageable, accountQuery);
        return page;
    }


    public List<Account> findByOfficeId(String officeId){
        List<Account> accountList=accountMapper.findByOfficeIds(Lists.newArrayList(officeId));
        return accountList;
    }

    public List<Account> findByOfficeIds(List<String> officeIds){
        List<Account> accountList=accountMapper.findByOfficeIds(officeIds);
        return accountList;
    }

    public List<Account> findByFilter(AccountQuery accountQuery){
        List<Account> accountList=accountMapper.findByFilter(accountQuery);
        return accountList;
    }

    @CachePut(value = "accounts",key="#p0.id")
    public Account save(Account account){
        accountMapper.save(account);
        return  account;
    }

    public Account findByLoginName(String loginName){
        Account account=accountMapper.findByLoginName(loginName);
        return account;
    }

    public List<Account> findByLoginNameLikeAndType(Map<String,Object> map){
        return accountMapper.findByLoginNameLikeAndType(map);
    }

    @CachePut(value = "accounts",key="#p0.id")
    public Account update(Account account){
        accountMapper.update(account);
        return  accountMapper.findOne(account.getId());
    }

    public int deleteAccountOffice(String accountId){
        return accountMapper.deleteAccountOffice(accountId);
    }

    public int saveAccountOffice(String accountId,List<String> officeIds){
        return accountMapper.saveAccountOffice(accountId,officeIds);
    }

    public void logicDeleteOne(String id) {
        accountMapper.logicDeleteOne(id);
    }
    
}
