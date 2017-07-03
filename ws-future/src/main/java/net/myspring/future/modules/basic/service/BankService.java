package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.kingdee.domain.CnBankAcnt;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
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
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class BankService {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CloudClient cloudClient;

    public List<BankDto> findByAccountId(String accountId){
        List<Bank> banks = bankRepository.findByAccountId(accountId);
        List<BankDto> bankDtoList = BeanUtil.map(banks, BankDto.class);
        return  bankDtoList==null?Lists.newArrayList():bankDtoList;
    }

    public Page<BankDto> findPage(Pageable pageable,BankQuery bankQuery) {
        Page<BankDto> page = bankRepository.findPage(pageable, bankQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void logicDelete(String id) {
        bankRepository.logicDelete(id);
    }

    @Transactional
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

    @Transactional
    public void syn(){
        List<CnBankAcnt> cnBankAcnts = cloudClient.getAllBank();
        List<Bank> banks = bankRepository.findAllEnabled();
        Map<String,Bank> bankOutIdMap = CollectionUtil.extractToMap(banks,"outId");
        Map<String,Bank> bankNameMap = CollectionUtil.extractToMap(banks,"name");
        List<Bank> saveBanks = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(cnBankAcnts)){
            for (CnBankAcnt cnBankacnt : cnBankAcnts) {
                Bank bank = bankOutIdMap.get(cnBankacnt.getFBankAcntId());
                if(bank==null) {
                    bank=bankNameMap.get(cnBankacnt.getFName());
                    if(bank==null){
                        bank = new Bank();
                        bank.setCreatedBy(RequestUtils.getAccountId());
                    }else{
                        bank.setLastModifiedBy(RequestUtils.getAccountId());
                    }
                } else {
                    bank.setLastModifiedBy(RequestUtils.getAccountId());
                }
                bank.setOldName(bank.getName());
                bank.setOldOutId(bank.getOutId());
                bank.setCompanyId(RequestUtils.getCompanyId());
                bank.setName(cnBankacnt.getFName());
                bank.setOutDate(cnBankacnt.getFModifyDate());
                bank.setOutId(cnBankacnt.getFBankAcntId());
                bank.setCode(cnBankacnt.getFNumber());
                saveBanks.add(bank);
            }
            bankRepository.save(saveBanks);
        }
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

    public List<BankDto> findByIds(List<String> ids){
        List<Bank> positionList = bankRepository.findAll(ids);
        List<BankDto> positionDtoList=BeanUtil.map(positionList,BankDto.class);
        return positionDtoList;
    }
}
