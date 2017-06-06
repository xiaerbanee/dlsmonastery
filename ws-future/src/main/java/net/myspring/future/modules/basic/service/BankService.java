package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.repository.BankRepository;
import net.myspring.future.modules.basic.web.form.BankForm;
import net.myspring.future.modules.basic.web.query.BankQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankService {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeClient officeClient;

    public List<BankDto> findByAccountId(String accountId){
        List<Bank> banks = bankRepository.findByAccountId(accountId);
        List<BankDto> bankDtoList = BeanUtil.map(banks, BankDto.class);
        return  bankDtoList==null?Lists.newArrayList():bankDtoList;
    }

    public Page<BankDto> findPage(Pageable pageable,BankQuery bankQuery) {
        bankQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        Page<BankDto> page = bankRepository.findPage(pageable, bankQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void logicDelete(String id) {
        bankRepository.logicDelete(id);
    }

    public void save(BankForm bankForm){
        Bank bank = bankRepository.findOne(bankForm.getId());
        bank.setName(bankForm.getName());
        bank.setRemarks(bankForm.getRemarks());
        bankRepository.save(bank);
        bankRepository.deleteBankAccount(bankForm.getId());
        if(CollectionUtil.isNotEmpty(bankForm.getAccountIdList())){
            bankRepository.saveBankAccount(bankForm.getId(),bankForm.getAccountIdList());
        }
    }


    public void syn(){
    }

    public List<BankDto> findByNameContaining(String name){
        List<Bank> banks = bankRepository.findByNameContaining(name);
        List<BankDto> bankDtos= BeanUtil.map(banks, BankDto.class);
        cacheUtils.initCacheInput(bankDtos);
        return bankDtos;
    }

    public BankDto findOne(String id){
        BankDto bankDto;
        if(StringUtils.isBlank(id)) {
            bankDto = new BankDto();
        } else {
            Bank bank= bankRepository.findOne(id);
            bankDto= BeanUtil.map(bank,BankDto.class);
            List<String> accountIdList = bankRepository.findAccountIdList(id);
            bankDto.setAccountIdList(accountIdList);
        }
        return bankDto;
    }
}
