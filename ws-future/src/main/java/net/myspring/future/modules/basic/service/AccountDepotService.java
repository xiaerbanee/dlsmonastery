package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.repository.AccountDepotRepository;
import net.myspring.future.modules.basic.web.form.AccountDepotForm;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDepotService {

    @Autowired
    private AccountDepotRepository accountDepotRepository;

    public List<String> findByAccountId(String accountId){
        return accountDepotRepository.findByAccountId(accountId);
    }

    public void save(AccountDepotForm accountDepotForm){
        accountDepotRepository.deleteByAccountId(accountDepotForm.getAccountId());
        if(CollectionUtil.isNotEmpty(accountDepotForm.getDepotIdList())){
            accountDepotRepository.save(accountDepotForm);
        }
    }
}
