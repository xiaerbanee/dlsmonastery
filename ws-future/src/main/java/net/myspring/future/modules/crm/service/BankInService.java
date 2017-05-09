package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.mapper.BankInMapper;
import net.myspring.future.modules.crm.web.form.BankInForm;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankInService {

    @Autowired
    private BankInMapper bankInMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;

    public Page<BankInDto> findPage(Pageable pageable, BankInQuery bankInQuery) {
        Page<BankInDto> page = bankInMapper.findPage(pageable, bankInQuery);
        if(page!=null && page.getContent()!=null){
            for(BankInDto each : page.getContent()){
                if(each.getShopParentId()!=null){
                    each.setRealShopName(depotMapper.findOne(each.getShopParentId()).getName());
                }else {
                    each.setRealShopName(each.getShopName());
                }
            }
        }
        cacheUtils.initCacheInput(page.getContent());
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

    public void audit(String id, boolean pass, String comment){
        String name="文件审批";
        BankIn bankIn = bankInMapper.findOne(id);
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(id, name, bankIn.getProcessInstanceId(), bankIn.getProcessTypeId(), comment, pass));
        bankIn.setLocked(true);
        bankIn.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        bankIn.setProcessStatus(activitiCompleteDto.getProcessStatus());
        bankIn.setPositionId(activitiCompleteDto.getPositionId());
        bankInMapper.update(bankIn);
    }

    public BankInForm findForm(BankInForm bankInForm) {
        BankIn bankIn=bankInMapper.findOne(bankInForm.getId());
        BankInForm result = BeanUtil.map(bankIn, BankInForm.class);

        if(result == null){
            result = new BankInForm();
        }

        return result;

    }

    public BankIn save(BankInForm bankInForm) {
        BankIn bankIn;
        if(bankInForm.isCreate()) {
            String name="销售收款";
            String businessKey = bankInForm.getId();
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm(name, businessKey,BankIn.class.getSimpleName(),bankInForm.getAmount().toString()));
            bankIn = BeanUtil.map(bankInForm, BankIn.class);
            bankIn.setProcessFlowId(activitiStartDto.getProcessFlowId());
            bankIn.setProcessStatus(activitiStartDto.getProcessStatus());
            bankIn.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
            bankIn.setPositionId(activitiStartDto.getPositionId());
            bankIn.setProcessTypeId(activitiStartDto.getProcessTypeId());
            bankInMapper.save(bankIn);
        } else {
            bankIn = bankInMapper.findOne(bankInForm.getId());
            ReflectionUtil.copyProperties(bankInForm,bankIn);
            bankInMapper.update(bankIn);
        }
        return bankIn;
    }

    public void logicDeleteOne(String id) {
        bankInMapper.logicDeleteOne(id);
    }

    public BankInDto findDetail(String id) {

        BankInDto result = bankInMapper.findDto(id);
        if(result.getShopParentId()!=null){
            result.setRealShopName(depotMapper.findOne(result.getShopParentId()).getName());
        }else {
            result.setRealShopName(result.getShopName());
        }
        cacheUtils.initCacheInput(result);
        return result;

    }
}
