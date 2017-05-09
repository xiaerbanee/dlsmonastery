package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.mapper.AccountKingdeeBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lihx on 2017/5/9.
 */
@Service
@LocalDataSource
public class AccountKingdeeBookService {
    @Autowired
    private AccountKingdeeBookMapper accountKingdeeBookMapper;

    public AccountKingdeeBook findByAccountId(String accountId){
        return accountKingdeeBookMapper.findByAccountId(accountId);
    }
}
