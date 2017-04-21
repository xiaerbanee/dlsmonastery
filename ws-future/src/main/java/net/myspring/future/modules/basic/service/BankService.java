package net.myspring.future.modules.basic.service;

import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.web.Query.BankQuery;
import net.myspring.future.modules.basic.web.form.BankForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BankService {

    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private CloudClient cloudClient;

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

    public Page<BankDto> findPage(Pageable pageable,BankQuery bankQuery) {
        bankQuery.setDepotIdList(depotManager.getDepotIds(SecurityUtils.getAccountId()));
        bankQuery.setOfficeIdList(officeClient.getOfficeFilterIds(SecurityUtils.getAccountId()));
        Page<BankDto> page = bankMapper.findPage(pageable, bankQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(Bank bank) {
        bank.setEnabled(false);
        bankMapper.update(bank);
    }

    public BankForm findForm(BankForm bankForm){
        if(!bankForm.isCreate()){
            Bank bank=bankMapper.findOne(bankForm.getId());
            bankForm= BeanUtil.map(bank,BankForm.class);
            cacheUtils.initCacheInput(bankForm);
        }
        return bankForm;
    }

    public void save(BankForm bankForm){
        bankMapper.updateForm(bankForm);
        bankMapper.deleteBankAccount(bankForm.getId());
        if(CollectionUtil.isNotEmpty(bankForm.getAccountIdList())){
            bankMapper.saveAccount(bankForm.getId(),bankForm.getAccountIdList());
        }
    }


    @Transactional
    public void syn(){
        String cloudName = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode());
        LocalDateTime dateTime=bankMapper.getMaxOutDate();
        String result = cloudClient.getSynBankData(cloudName, LocalDateTimeUtils.format(dateTime));
        List<Map<String, Object>> dataList = ObjectMapperUtils.readValue(result,List.class);
        if(CollectionUtil.isNotEmpty(dataList)){
            for(Map<String,Object> map:dataList){
                Bank bank = bankMapper.findByOutId(map.get("outId").toString());
                if(bank==null) {
                    bank=bankMapper.findByName(map.get("name").toString());
                    if(bank==null){
                        bank = new Bank();
                        bankMapper.save(bank);
                    }
                }
                bank.setName(map.get("name").toString());
                bank.setOutDate(LocalDateTimeUtils.parse(map.get("modifyDate").toString()));
                bank.setOutId(map.get("outId").toString());
                bank.setCode(map.get("code").toString());
                bankMapper.update(bank);
            }
        }
    }

}
