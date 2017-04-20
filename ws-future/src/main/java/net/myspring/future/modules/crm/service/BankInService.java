package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.mapper.BankInMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class BankInService {

    @Autowired
    private BankInMapper bankInMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private BankMapper bankMapper;

    public Page<BankIn> findPage(Pageable pageable, Map<String, Object> map) {
        Page<BankIn> page = bankInMapper.findPage(pageable, map);
        return page;
    }

    public BankIn findOne(String id){
        BankIn bankIn=bankInMapper.findOne(id);
        return bankIn;
    }

    public void delete(BankIn bankIn) {
        bankIn.setEnabled(false);
        bankInMapper.update(bankIn);
    }

    @Transactional
    public void save(BankIn bankIn){
    }

    @Transactional
    public void audit(BankIn bankIn){
    }

}
