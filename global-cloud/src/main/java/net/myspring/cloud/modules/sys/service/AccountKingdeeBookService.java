package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.dto.AccountKingdeeBookDto;
import net.myspring.cloud.modules.sys.mapper.AccountKingdeeBookMapper;
import net.myspring.cloud.modules.sys.web.query.AccountKingdeeBookQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by lihx on 2017/5/9.
 */
@Service
@LocalDataSource
public class AccountKingdeeBookService {
    @Autowired
    private AccountKingdeeBookMapper accountKingdeeBookMapper;

    public Page<AccountKingdeeBookDto> findPage(Pageable pageable, AccountKingdeeBookQuery accountKingdeeBookQuery){
        Page<AccountKingdeeBookDto> page = accountKingdeeBookMapper.findPage(pageable,accountKingdeeBookQuery);
        return page;
    }

    public AccountKingdeeBook findByAccountId(String accountId){
        return accountKingdeeBookMapper.findByAccountId(accountId);
    }
}
