package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AccountChangeTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.repository.AccountChangeRepository;
import net.myspring.basic.modules.hr.repository.AccountRepository;
import net.myspring.basic.modules.hr.repository.EmployeeRepository;
import net.myspring.basic.modules.hr.repository.PositionRepository;
import net.myspring.basic.modules.hr.web.form.AccountChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.basic.modules.sys.client.ActivitiClient;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.repository.OfficeRepository;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = false)
public class AccountChangeService {

    @Autowired
    private AccountChangeRepository accountChangeRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;


    public AccountChange findOne(String id){
        AccountChange accountChange=accountChangeRepository.findOne(id);
        return accountChange;
    }

    public AccountChangeForm getForm(AccountChangeQuery accountChangeQuery){
        AccountChangeForm accountChangeForm =new AccountChangeForm();
        if(StringUtils.isNotBlank(accountChangeQuery.getId())||StringUtils.isNotBlank(accountChangeQuery.getAccountId())){
            accountChangeForm = accountChangeRepository.getForm(accountChangeQuery);
            if(StringUtils.isNotBlank(accountChangeQuery.getId())){
                AccountChange accountChange=accountChangeRepository.findOne(accountChangeQuery.getId());
                accountChangeForm.setType(accountChange.getType());
                accountChangeForm.setNewValue(accountChange.getNewValue());
                accountChangeForm.setRemarks(accountChange.getRemarks());
            }
            cacheUtils.initCacheInput(accountChangeForm);
        }
        return accountChangeForm;
    }

    public void audit(String id,boolean pass,String comment) {
        AccountChange accountChange = accountChangeRepository.findOne(id);
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(accountChange.getProcessInstanceId(), accountChange.getProcessTypeId(), comment, pass));
        accountChange.setLocked(true);
        accountChange.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        accountChange.setProcessStatus(activitiCompleteDto.getProcessStatus());
        accountChange.setPositionId(activitiCompleteDto.getPositionId());
        accountChangeRepository.save(accountChange);
        if (AuditTypeEnum.PASSED.name().equals(accountChange.getProcessStatus())) {
            Account account = accountRepository.findOne(accountChange.getAccountId());
            Employee employee=employeeRepository.findOne(account.getEmployeeId());
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
            }else if(accountChange.getType().equals(AccountChangeTypeEnum.ENTRY_WORKER.toString())){
                employee.setEntryDate(LocalDateUtils.parse(accountChange.getNewValue()));
            }else if(accountChange.getType().equals(AccountChangeTypeEnum.BASE_SALARY.toString())){
                employee.setSalary(new BigDecimal(accountChange.getNewValue()));
            }
            accountRepository.save(account);
            employeeRepository.save(employee);
        }
    }

    public AccountChange save(AccountChangeForm accountChangeForm) {
        Account account=accountRepository.findOne(accountChangeForm.getAccountId());
        Employee employee=employeeRepository.findOne(account.getEmployeeId());
        AccountChange accountChange=new AccountChange();
        accountChange.setAccountId(accountChangeForm.getAccountId());
        accountChange.setNewValue(accountChangeForm.getNewValue());
        accountChange.setType(accountChangeForm.getType());
        accountChange.setRemarks(accountChangeForm.getRemarks());
        if (accountChange.getType().equals(AccountChangeTypeEnum.OFFICE.toString())) {
            if (StringUtils.isNotBlank(account.getOfficeId())) {
                Office office=officeRepository.findOne(account.getOfficeId());
                accountChange.setOldValue(office.getId());
                accountChange.setOldLabel(office.getName());
            }
            accountChange.setNewLabel(officeRepository.findOne(accountChange.getNewValue()).getName());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.POSITION.toString())) {
            if (StringUtils.isNotBlank(account.getPositionId())) {
                Position position=positionRepository.findOne(account.getPositionId());
                accountChange.setOldValue(position.getId());
                accountChange.setOldLabel(position.getName());
            }
            accountChange.setNewLabel(positionRepository.findOne(accountChange.getNewValue()).getName());
        } else if (accountChange.getType().equals(AccountChangeTypeEnum.LEADER.toString())) {
            if (StringUtils.isNotBlank(account.getLeaderId())) {
                Account leader=accountRepository.findOne(account.getLeaderId());
                accountChange.setOldValue(leader.getId());
                accountChange.setOldLabel(leader.getLoginName());
            }
            accountChange.setNewLabel(accountRepository.findOne(accountChange.getNewValue()).getLoginName());
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
        accountChangeRepository.save(accountChange);
        String businessKey = accountChange.getId();
        ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("员工调整", businessKey,AccountChange.class.getSimpleName(),account.getLoginName()));
        accountChange.setProcessStatus(activitiStartDto.getProcessStatus());
        accountChange.setProcessFlowId(activitiStartDto.getProcessFlowId());
        accountChange.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
        accountChange.setPositionId(activitiStartDto.getPositionId());
        accountChange.setProcessTypeId(activitiStartDto.getProcessTypeId());
        accountChangeRepository.save(accountChange);
        return accountChange;
    }


    public Page<AccountChangeDto> findPage(Pageable pageable, AccountChangeQuery accountChangeQuery){
        Page<AccountChangeDto> page=accountChangeRepository.findPage(pageable,accountChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void logicDelete(String id){
        accountChangeRepository.logicDelete(id);
    }
}
