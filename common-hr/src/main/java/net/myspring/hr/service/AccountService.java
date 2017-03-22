package net.myspring.hr.service;

import net.myspring.hr.domain.Account;
import net.myspring.hr.mapper.AccountMapper;
import net.myspring.hr.web.form.AccountForm;
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
        AccountForm accountForm = new AccountForm();
        accountForm.setRemarks(UUID.randomUUID().toString());
        accountForm.setName(UUID.randomUUID().toString());
        int result = accountMapper.save(accountForm);
        System.out.println(result);
        return accountForm;
    }
}
