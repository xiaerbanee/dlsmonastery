package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.mapper.BankInMapper;
import net.myspring.future.modules.crm.web.form.BankInDetailForm;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class BankInService {

    @Autowired
    private BankInMapper bankInMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;

    public Page<BankInDto> findPage(Pageable pageable, BankInQuery bankInQuery) {
        Page<BankInDto> page = bankInMapper.findPage(pageable, bankInQuery);

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

    public void audit(BankInDetailForm bankInDetailForm){
        //TODO 需要同步金蝶
        BankIn bankIn = bankInMapper.findOne(bankInDetailForm.getId());
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(bankIn.getProcessInstanceId(), bankIn.getProcessTypeId(), bankInDetailForm.getAuditRemarks(), "1".equals(bankInDetailForm.getPass())));
        if("已通过".equals(activitiCompleteDto.getProcessStatus())){
            bankIn.setLocked(true);
        }

        bankIn.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        bankIn.setProcessStatus(activitiCompleteDto.getProcessStatus());
        bankIn.setPositionId(activitiCompleteDto.getPositionId());
        bankIn.setBillDate(bankInDetailForm.getBillDate() == null ? LocalDate.now() : bankInDetailForm.getBillDate());
        bankInMapper.update(bankIn);

    }

    public BankInForm getFormProperty(BankInForm bankInForm) {
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
            bankIn = BeanUtil.map(bankInForm, BankIn.class);
            bankInMapper.save(bankIn);

            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("销售收款",  bankIn.getId(),BankIn.class.getSimpleName(),bankIn.getAmount().toString()));
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

    public BankInDetailForm findDetail(String id, String action) {

        BankInDetailForm result = new BankInDetailForm();
        result.setId(id);
        BankInDto bankInDto = bankInMapper.findDto(id);
        result.setBankInDto(bankInDto);

        if("audit".equals(action)){
            result.setBillDate(LocalDate.now());
        }

        cacheUtils.initCacheInput(result);
        return result;
    }

    public void batchAudit(String[] ids, String pass) {
        if(ids == null){
            return;
        }
        List<String> idList = Arrays.asList(ids);
        for(String id : idList){
            BankInDetailForm bankInDetailForm = new BankInDetailForm();
            bankInDetailForm.setAuditRemarks("批量通过");
            bankInDetailForm.setId(id);
            bankInDetailForm.setPass(pass);
            bankInDetailForm.setSyn("1");
            bankInDetailForm.setBillDate(LocalDate.now());

            audit(bankInDetailForm);

        }

    }
}
