package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.repository.AccountDepotRepository;
import net.myspring.future.modules.basic.web.form.DepotAccountForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class AccountDepotService {

    @Autowired
    private AccountDepotRepository accountDepotRepository;

    public List<String> findByAccountId(String accountId){
        return accountDepotRepository.findByAccountId(accountId);
    }

    public List<String> findByDepotId(String accountId){
        return accountDepotRepository.findByDepotId(accountId);
    }

    @Transactional
    public void save(DepotAccountForm depotAccountForm){
        if(StringUtils.isNotBlank(depotAccountForm.getDepotId())){
            accountDepotRepository.deleteByDepotId(depotAccountForm.getDepotId());
        }else if(StringUtils.isNotBlank(depotAccountForm.getAccountId())){
            accountDepotRepository.deleteByAccountId(depotAccountForm.getAccountId());
        }
        if(CollectionUtil.isNotEmpty(depotAccountForm.getDepotIdList())||CollectionUtil.isNotEmpty(depotAccountForm.getAccountIdList())){
            accountDepotRepository.save(depotAccountForm);
        }
    }
}
