package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.repository.BdAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 账户（科目）
 * Created by lihx on 2017/6/8.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdAccountService {
    @Autowired
    private BdAccountRepository bdAccountRepository;

    public BdAccount findByNumber(String number){
        return bdAccountRepository.findByNumber(number);
    }

    public BdAccount findByName(String name){
        return bdAccountRepository.findByName(name);
    }

    public List<BdAccount> findAll(){
        return bdAccountRepository.findAll();
    }
}
