package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BankService {

    @Autowired
    private BankMapper bankMapper;

    public Bank findOne(String id){
        Bank bank=bankMapper.findOne(id);
        initDomain(Lists.newArrayList(bank));
        return bank;
    }

    public List<Bank> findByAccountId(String accountId){
            return  bankMapper.findByAccountId(accountId);
    }

    public List<Bank> findAll(){
        return bankMapper.findAll();
    }

    public Page<Bank> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Bank> page = bankMapper.findPage(pageable, map);
        initDomain(page.getContent());
        return page;
    }

    public void delete(Bank bank) {
        bank.setEnabled(false);
        bankMapper.update(bank);
    }

    @Transactional
    public void save(Bank bank){
        bankMapper.update(bank);
        bankMapper.deleteBankAccount(bank.getId());
    }


    @Transactional
    public void syn(){
    }

    private void initDomain(List<Bank> bankList){
    }
}
