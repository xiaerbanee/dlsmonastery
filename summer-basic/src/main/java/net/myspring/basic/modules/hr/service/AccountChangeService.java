package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.AccountChangeTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.*;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.mapper.*;
import net.myspring.basic.modules.hr.web.form.AccountChangeForm;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.basic.modules.sys.client.ActivitiClient;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class AccountChangeService {

    @Autowired
    private AccountChangeMapper accountChangeMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;


    public AccountChange findOne(String id){
        AccountChange accountChange=accountChangeMapper.findOne(id);
        return accountChange;
    }

    public AccountChangeForm getForm(AccountChangeQuery accountChangeQuery){
        AccountChangeForm accountChangeForm =new AccountChangeForm();
        if(StringUtils.isNotBlank(accountChangeQuery.getId())||StringUtils.isNotBlank(accountChangeQuery.getAccountId())){
            accountChangeForm = accountChangeMapper.getForm(accountChangeQuery);
            if(StringUtils.isNotBlank(accountChangeQuery.getId())){
                AccountChange accountChange=accountChangeMapper.findOne(accountChangeQuery.getId());
                accountChangeForm.setType(accountChange.getType());
            }
            cacheUtils.initCacheInput(accountChangeForm);
        }
        return accountChangeForm;
    }

    public void audit(String id,boolean pass,String comment) {
        AccountChange accountChange = accountChangeMapper.findOne(id);
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(accountChange.getProcessInstanceId(), accountChange.getProcessTypeId(), comment, pass));
        accountChange.setLocked(true);
        accountChange.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        accountChange.setProcessStatus(activitiCompleteDto.getProcessStatus());
        accountChange.setPositionId(activitiCompleteDto.getPositionId());
        accountChangeMapper.update(accountChange);
        if (AuditTypeEnum.PASSED.name().equals(accountChange.getProcessStatus())) {
            Account account = accountMapper.findOne(accountChange.getAccountId());
            Employee employee=employeeMapper.findOne(account.getEmployeeId());
            if (accountChange.getType().equals(AccountChangeTypeEnum.OFFICE.toString())) {
                account.setOfficeId(accountChange.getNewValue());
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.POSITION.toString())) {
                account.setPositionId(accountChange.getNewValue());
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.LEADER.toString())) {
                account.setLeaderId(accountChange.getNewValue());
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.MOBILE_PHONE.toString())) {
                employee.setMobilePhone(accountChange.getNewValue());
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.ID_CARD.toString())) {
                employee.setIdcard(accountChange.getNewValue());
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.BANK_CARD.toString())) {
                employee.setBankNumber(accountChange.getNewValue());
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.REGULAR_WORKER.toString())) {
                employee.setRegularDate(LocalDateUtils.parse(accountChange.getNewValue()));
            } else if (accountChange.getType().equals(AccountChangeTypeEnum.LEAVE_WORKER.toString())) {
                employee.setLeaveDate(LocalDateUtils.parse(accountChange.getNewValue()));
            }else if(accountChange.getType().equals(AccountChangeTypeEnum.ENTRY_WORKER.name())){
                employee.setEntryDate(LocalDateUtils.parse(accountChange.getNewValue()));
            }else if(accountChange.getType().equals(AccountChangeTypeEnum.BASE_SALARY.name())){
                employee.setSalary(new BigDecimal(accountChange.getNewValue()));
            }
            accountMapper.update(account);
            employeeMapper.update(employee);
        }
    }

    public AccountChange save(AccountChangeForm accountChangeForm) {
        Account account=accountMapper.findOne(accountChangeForm.getAccountId());
        Employee employee=employeeMapper.findOne(account.getEmployeeId());
        AccountChange accountChange=new AccountChange();
        accountChange.setAccountId(accountChange.getAccountId());
        accountChange.setNewValue(accountChange.getNewValue());
        if (accountChangeForm.getType().equals(AccountChangeTypeEnum.OFFICE.toString())) {
            if (StringUtils.isNotBlank(account.getOfficeId())) {
                Office office=officeMapper.findOne(account.getOfficeId());
                accountChange.setOldValue(office.getId());
                accountChange.setOldLabel(office.getName());
            }
            accountChange.setNewLabel(officeMapper.findOne(accountChange.getNewValue()).getName());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.POSITION.toString())) {
            if (StringUtils.isNotBlank(account.getPositionId())) {
                Position position=positionMapper.findOne(account.getPositionId());
                accountChange.setOldValue(position.getId());
                accountChange.setOldLabel(position.getName());
            }
            accountChange.setNewLabel(positionMapper.findOne(accountChange.getNewValue()).getName());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.LEADER.toString())) {
            if (StringUtils.isNotBlank(account.getLeaderId())) {
                Account leader=accountMapper.findOne(account.getLeaderId());
                accountChange.setOldValue(leader.getId());
                accountChange.setOldLabel(leader.getLoginName());
            }
            accountChange.setNewLabel(accountMapper.findOne(accountChange.getNewValue()).getLoginName());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.MOBILE_PHONE.toString())) {
            accountChange.setOldLabel(employee.getMobilePhone());
            accountChange.setOldValue(employee.getMobilePhone());
            accountChange.setNewLabel(accountChangeForm.getNewValue());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.ID_CARD.toString())) {
            accountChange.setOldLabel(employee.getIdcard());
            accountChange.setOldValue(employee.getIdcard());
            accountChange.setNewLabel(accountChangeForm.getNewValue());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.BANK_CARD.toString())) {
            accountChange.setOldLabel(employee.getBankNumber());
            accountChange.setOldValue(employee.getBankNumber());
            accountChange.setNewLabel(accountChangeForm.getNewValue());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.BASE_SALARY.toString())) {
            accountChange.setOldLabel(employee.getSalary()!=null?employee.getSalary().toString():null);
            accountChange.setOldValue(employee.getSalary()!=null?employee.getSalary().toString():null);
            accountChange.setNewLabel(accountChangeForm.getNewValue());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.REGULAR_WORKER.toString())) {
            if (employee.getRegularDate() != null) {
                accountChange.setOldLabel(LocalDateUtils.format(employee.getRegularDate()));
                accountChange.setOldValue(LocalDateUtils.format(employee.getRegularDate()));
                accountChange.setNewLabel(accountChangeForm.getNewValue());
            }
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.LEAVE_WORKER.toString())) {
            if (employee.getLeaveDate() != null) {
                accountChange.setOldLabel(LocalDateUtils.format(employee.getLeaveDate()));
                accountChange.setOldValue(LocalDateUtils.format(employee.getLeaveDate()));
                accountChange.setNewLabel(accountChangeForm.getNewValue());
            }
        }else if(accountChange.getType().equals(AccountChangeTypeEnum.ENTRY_WORKER.name())){
            if (employee.getEntryDate() != null) {
                accountChange.setOldLabel(LocalDateUtils.format(employee.getEntryDate()));
                accountChange.setOldValue(LocalDateUtils.format(employee.getEntryDate()));
            }
            accountChange.setNewLabel(accountChangeForm.getNewValue());
        }
        accountChangeMapper.save(accountChange);
        String businessKey = accountChange.getId();
        ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("员工调整", businessKey,AccountChange.class.getSimpleName(),account.getLoginName()));
        accountChange.setProcessStatus(activitiStartDto.getProcessStatus());
        accountChange.setProcessFlowId(activitiStartDto.getProcessFlowId());
        accountChange.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
        accountChange.setPositionId(activitiStartDto.getPositionId());
        accountChange.setProcessTypeId(activitiStartDto.getProcessTypeId());
        accountChangeMapper.update(accountChange);
        return accountChange;
    }


    public Page<AccountChangeDto> findPage(Pageable pageable, AccountChangeQuery accountChangeQuery){
        Page<AccountChangeDto> page=accountChangeMapper.findPage(pageable,accountChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }
}
