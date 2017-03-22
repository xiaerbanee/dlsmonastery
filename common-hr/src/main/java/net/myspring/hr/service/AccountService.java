package net.myspring.hr.service;

import net.myspring.hr.domain.Account;
import net.myspring.hr.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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

    public Account save() {
        Account account = accountMapper.findOne("1");
        account.setRemarks(UUID.randomUUID().toString());
        int result = accountMapper.update(account);
        System.out.println(result);
        return account;
    }
}
