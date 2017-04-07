package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.AccountTask;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.basic.modules.hr.dto.OfficeChangeDto;
import net.myspring.basic.modules.hr.mapper.AccountTaskMapper;
import net.myspring.basic.modules.hr.mapper.OfficeChangeMapper;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
import net.myspring.basic.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class OfficeChangeService {

    @Autowired
    private OfficeChangeMapper officeChangeMapper;
    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private AccountTaskMapper accountTaskMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public OfficeChange findOne(String id){
        OfficeChange officeChange=officeChangeMapper.findOne(id);
        return officeChange;
    }

    public OfficeChangeDto findDto(String id){
        OfficeChange officeChange=findOne(id);
        OfficeChangeDto officeChangeDto= BeanUtil.map(officeChange,OfficeChangeDto.class);
        cacheUtils.initCacheInput(officeChangeDto);
        return officeChangeDto;
    }

    public void notify(OfficeChange officeChange) {
        String name="机构调整";
        AccountTask accountTask = accountTaskMapper.findByNameAndExtendId(name,officeChange.getId());
        if(accountTask==null){
            accountTask =new AccountTask();
            accountTask.setName(name);
            accountTask.setExtendId(officeChange.getId());
            accountTask.setPositionId(officeChange.getProcessFlow().getPositionId());
            accountTask.setOfficeId(securityUtils.getOfficeId());
            accountTaskMapper.save(accountTask);
        }else {
            if(AuditTypeEnum.PASS.getValue().equals(officeChange.getProcessStatus()) ||AuditTypeEnum.NOT_PASS.getValue().equals(officeChange.getProcessStatus())){
                accountTask.setStatus("已通过");
                accountTask.setEnabled(false);
            }else{
                accountTask.setPositionId(officeChange.getProcessFlow().getPositionId());
                accountTask.setStatus(officeChange.getProcessStatus());
            }
            accountTaskMapper.update(accountTask);
        }
    }
}
