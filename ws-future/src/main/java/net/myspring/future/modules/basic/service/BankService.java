package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.web.Query.BankQuery;
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
        return bank;
    }

    public List<Bank> findByAccountId(String accountId){
            return  bankMapper.findByAccountId(accountId);
    }

    public List<Bank> findAll(){
        return bankMapper.findAll();
    }

    public Page<BankDto> findPage(Pageable pageable, BankQuery bankQuery) {
        Page<BankDto> page = bankMapper.findPage(pageable, bankQuery);
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

}
