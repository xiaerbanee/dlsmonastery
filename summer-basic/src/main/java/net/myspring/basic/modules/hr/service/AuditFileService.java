package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.AccountTask;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.mapper.AccountTaskMapper;
import net.myspring.basic.modules.hr.mapper.AuditFileMapper;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.basic.modules.sys.mapper.ProcessFlowMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditFileService {

    @Autowired
    private AuditFileMapper auditFileMapper;
    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private AccountTaskMapper accountTaskMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<AuditFileDto> findPage(Pageable pageable, AuditFileQuery auditFileQuery){
        Page<AuditFileDto> page=auditFileMapper.findPage(pageable,auditFileQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AuditFile findOne(String id){
        AuditFile auditFile=auditFileMapper.findOne(id);
        return auditFile;
    }

    public AuditFileForm findForm(String id){
        AuditFile auditFile=auditFileMapper.findOne(id);
        AuditFileForm auditFileForm= BeanUtil.map(auditFile,AuditFileForm.class);
        cacheUtils.initCacheInput(auditFileForm);
        return auditFileForm;
    }

    public void notify(AuditFile auditFile) {
        String name = "文件审批";
        AccountTask accountTask = accountTaskMapper.findByNameAndExtendId(name,auditFile.getId());
        if(accountTask==null){
            accountTask =new AccountTask();
            accountTask.setName(name);
            accountTask.setExtendId(auditFile.getId());
            accountTask.setPositionId(auditFile.getProcessFlow().getPositionId());
            accountTask.setOfficeId(SecurityUtils.getOfficeId());
            accountTaskMapper.save(accountTask);
        }else {
            if(AuditTypeEnum.PASS.getValue().equals(auditFile.getProcessStatus()) ||AuditTypeEnum.NOT_PASS.getValue().equals(auditFile.getProcessStatus())){
                accountTask.setStatus("已审核");
                accountTask.setEnabled(false);
            }else{
                accountTask.setPositionId(auditFile.getProcessFlow().getPositionId());
                accountTask.setStatus(auditFile.getProcessStatus());
            }
            accountTaskMapper.update(accountTask);
        }
    }

    public void logicDeleteOne(String id) {
        auditFileMapper.logicDeleteOne(id);
    }
}
