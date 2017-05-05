package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.mapper.AuditFileMapper;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.basic.modules.sys.client.ActivitiClient;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
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
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private OfficeService officeService;


    public Page<AuditFileDto> findPage(Pageable pageable, AuditFileQuery auditFileQuery) {
        Page<AuditFileDto> page = auditFileMapper.findPage(pageable, auditFileQuery);
        for(AuditFileDto auditFileDto:page.getContent()){
            auditFileDto.setAreaId(officeService.findByOfficeIdAndRuleName(auditFileDto.getOfficeId(),"办事处"));
        }
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AuditFile findOne(String id) {
        AuditFile auditFile = auditFileMapper.findOne(id);
        return auditFile;
    }

    public AuditFileForm findForm(AuditFileForm auditFileForm) {
        if (!auditFileForm.isCreate()) {
            AuditFile auditFile = auditFileMapper.findOne(auditFileForm.getId());
            auditFileForm = BeanUtil.map(auditFile, AuditFileForm.class);
            auditFileForm.setActivitiDetailList(activitiClient.getActivitiDetail(auditFile.getProcessInstanceId()));
            cacheUtils.initCacheInput(auditFileForm);
        }
        return auditFileForm;
    }

    public AuditFile save(AuditFileForm auditFileForm) {
        AuditFile auditFile;
        if (auditFileForm.isCreate()) {
            String name="文件审批";
            String businessKey = auditFileForm.getId();
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm(name, businessKey, auditFileForm.getProcessTypeId(),auditFileForm.getTitle()));
            auditFileForm.setProcessStatus(activitiStartDto.getProcessStatus());
            auditFileForm.setProcessFlowId(activitiStartDto.getProcessFlowId());
            auditFileForm.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
            auditFileForm.setPositionId(activitiStartDto.getPositionId());
            auditFile = BeanUtil.map(auditFileForm, AuditFile.class);
            auditFileMapper.save(auditFile);
            return auditFile;
        }
        return null;
    }

    public void audit(String id, boolean pass, String comment) {
        String name="文件审批";
        AuditFile auditFile = auditFileMapper.findOne(id);
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(id, name, auditFile.getProcessInstanceId(), auditFile.getProcessTypeId(), comment, pass));
        AuditFileForm auditFileForm = BeanUtil.map(auditFile, AuditFileForm.class);
        auditFileForm.setLocked(true);
        auditFileForm.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        auditFileForm.setProcessStatus(activitiCompleteDto.getProcessStatus());
        auditFileForm.setPositionId(activitiCompleteDto.getPositionId());
        auditFileMapper.updateForm(auditFileForm);

    }

    public void logicDeleteOne(String id) {
        auditFileMapper.logicDeleteOne(id);
    }
}
