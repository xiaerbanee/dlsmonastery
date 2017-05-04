package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.web.form.BankForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
@CacheConfig(cacheNames = "banks")
public class BankManager {
    @Autowired
    private BankMapper bankMapper;

    @Cacheable(key="#p0")
    public Bank findOne(String id) {
        return bankMapper.findOne(id);
    }

    @Cacheable(key="#p0.id")
    public Bank save(Bank bank){
        bankMapper.save(bank);
        return  bank;
    }

    @CachePut(key="#p0.id")
    public Bank update(Bank bank){
        bankMapper.update(bank);
        return  bankMapper.findOne(bank.getId());
    }

    @CachePut(key="#p0.id")
    public Bank updateForm(BankForm bankForm){
        bankMapper.updateForm(bankForm);
        return  bankMapper.findOne(bankForm.getId());
    }
}
