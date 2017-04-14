package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.modules.hr.domain.AccountTask;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.mapper.*;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.basic.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AccountTaskMapper accountTaskMapper;
    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public AccountChange findOne(String id){
        AccountChange accountChange=accountChangeMapper.findOne(id);
        return accountChange;
    }

    public AccountChangeDto findDto(String id){
        AccountChange accountChange=findOne(id);
        AccountChangeDto accountChangeDto= BeanUtil.map(accountChange,AccountChangeDto.class);
        cacheUtils.initCacheInput(accountChangeDto);
        return accountChangeDto;
    }

    public Page<AccountChangeDto> findPage(Pageable pageable, AccountChangeQuery accountChangeQuery){
        Page<AccountChange> page=accountChangeMapper.findPage(pageable,accountChangeQuery);
        Page<AccountChangeDto> accountChangeDtoPage= BeanUtil.map(page,AccountChangeDto.class);
        cacheUtils.initCacheInput(accountChangeDtoPage.getContent());
        return accountChangeDtoPage;
    }



    // 通知审批人
    public void notify(AccountChange accountChange) {
        String name = "员工信息调整";
        AccountTask accountTask = accountTaskMapper.findByNameAndExtendId(name,accountChange.getId());
        if(accountTask==null){
            accountTask =new AccountTask();
            accountTask.setName(name);
            accountTask.setExtendId(accountChange.getId());
            accountTask.setPositionId(accountChange.getProcessFlow().getPositionId());
            accountTask.setOfficeId(SecurityUtils.getOfficeId());
            accountTaskMapper.save(accountTask);
        }else {
            if(AuditTypeEnum.PASS.getValue().equals(accountChange.getProcessStatus()) ||AuditTypeEnum.NOT_PASS.getValue().equals(accountChange.getProcessStatus())){
                accountTask.setStatus("已通过");
                accountTask.setEnabled(false);
            }else{
                accountTask.setPositionId(accountChange.getProcessFlow().getPositionId());
                accountTask.setStatus(accountChange.getProcessStatus());
            }
            accountTaskMapper.update(accountTask);
        }
    }
}
