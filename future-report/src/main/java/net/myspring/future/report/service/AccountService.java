package net.myspring.future.report.service;

import net.myspring.future.report.manager.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/3/28.
 */
@Service
public class AccountService {
    @Autowired
    private AccountManager accountManager;

    public String findOne(String id) {
        return accountManager.findOne(id);
    }
}
