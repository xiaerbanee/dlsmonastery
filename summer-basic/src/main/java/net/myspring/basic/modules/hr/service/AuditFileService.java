package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.repository.AuditFileRepository;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.basic.modules.sys.client.ActivitiClient;
import net.myspring.basic.modules.sys.manager.OfficeManager;
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
public class AuditFileService {

    @Autowired
    private AuditFileRepository auditFileRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private OfficeManager officeManager;


    public Page<AuditFileDto> findPage(Pageable pageable, AuditFileQuery auditFileQuery) {
        Page<AuditFileDto> page = auditFileRepository.findPage(pageable, auditFileQuery);
        for(AuditFileDto auditFileDto:page.getContent()){
            auditFileDto.setAreaId(officeManager.findByOfficeIdAndRuleName(auditFileDto.getOfficeId(),"办事处"));
        }
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AuditFile findOne(String id) {
        AuditFile auditFile = auditFileRepository.findOne(id);
        return auditFile;
    }

    public AuditFileDto findOne(AuditFileDto auditFileDto) {
        if (!auditFileDto.isCreate()) {
            AuditFile auditFile = auditFileRepository.findOne(auditFileDto.getId());
            auditFileDto = BeanUtil.map(auditFile, AuditFileDto.class);
            auditFileDto.setActivitiDetailList(activitiClient.getActivitiDetail(auditFile.getProcessInstanceId()));
            cacheUtils.initCacheInput(auditFileDto);
        }
        return auditFileDto;
    }

    public AuditFile save(AuditFileForm auditFileForm) {
        AuditFile auditFile;
        if (auditFileForm.isCreate()) {
            String name="文件审批";
            String businessKey = auditFileForm.getId();
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm(name, businessKey, auditFileForm.getProcessTypeName(),auditFileForm.getTitle()));
            auditFile = BeanUtil.map(auditFileForm, AuditFile.class);
            auditFile.setProcessStatus(activitiStartDto.getProcessStatus());
            auditFile.setProcessFlowId(activitiStartDto.getProcessFlowId());
            auditFile.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
            auditFile.setPositionId(activitiStartDto.getPositionId());
            auditFile.setProcessTypeId(activitiStartDto.getProcessTypeId());
            auditFileRepository.save(auditFile);
            return auditFile;
        }
        return null;
    }

    public void audit(String id, boolean pass, String comment) {
        AuditFile auditFile = auditFileRepository.findOne(id);
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(auditFile.getProcessInstanceId(), auditFile.getProcessTypeId(), comment, pass));
        AuditFileForm auditFileForm = BeanUtil.map(auditFile, AuditFileForm.class);
        auditFileForm.setLocked(true);
        auditFileForm.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        auditFileForm.setProcessStatus(activitiCompleteDto.getProcessStatus());
        auditFileForm.setPositionId(activitiCompleteDto.getPositionId());
        ReflectionUtil.copyProperties(auditFileForm,auditFile);
        auditFileRepository.save(auditFile);

    }

    public void logicDelete(String id) {
        auditFileRepository.logicDelete(id);
    }
}
