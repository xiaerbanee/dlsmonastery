package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.client.ProcessTypeClient;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.mapper.AuditFileMapper;
import net.myspring.basic.modules.sys.client.ActivitiClient;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.general.modules.sys.dto.ActivitiAuditDto;
import net.myspring.general.modules.sys.dto.ActivitiAuthenticatedDto;
import net.myspring.general.modules.sys.form.ActivitiAuditForm;
import net.myspring.general.modules.sys.form.ActivitiAuthenticatedForm;
import net.myspring.general.modules.sys.form.ActivitiNotifyForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuditFileService {

    @Autowired
    private AuditFileMapper auditFileMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private ProcessTypeClient processTypeClient;


    public Page<AuditFileDto> findPage(Pageable pageable, AuditFileQuery auditFileQuery) {
        Page<AuditFileDto> page = auditFileMapper.findPage(pageable, auditFileQuery);
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
            cacheUtils.initCacheInput(auditFileForm);
        }
        auditFileForm.setProcessTypeList(processTypeClient.findAll());
        return auditFileForm;
    }

    public AuditFile save(AuditFileForm auditFileForm) {
        AuditFile auditFile;
        if (auditFileForm.isCreate()) {
            String name="文件审批";
            String businessKey = auditFileForm.getId();
            ActivitiAuthenticatedDto authenticated = activitiClient.authenticated(new ActivitiAuthenticatedForm(auditFileForm.getId(), name, businessKey, auditFileForm.getProcessTypeId(), SecurityUtils.getAccountId(),SecurityUtils.getOfficeId(),SecurityUtils.getCompanyId()));
            auditFileForm.setProcessStatus(authenticated.getProcessStatus());
            auditFileForm.setProcessFlowId(authenticated.getProcessFlowId());
            auditFileForm.setPositionId(authenticated.getPositionId());
            auditFile = BeanUtil.map(auditFileForm, AuditFile.class);
            auditFileMapper.save(auditFile);
            activitiClient.notify(new ActivitiNotifyForm(name,auditFile.getId(),auditFile.getProcessStatus(),SecurityUtils.getPositionId(),SecurityUtils.getCompanyId(),SecurityUtils.getAccountId(),SecurityUtils.getOfficeId()));
            return auditFile;
        }
        return null;
    }

    public void audit(String id, boolean pass, String comment) {
        String name="文件审批";
        AuditFile auditFile = auditFileMapper.findOne(id);
        ActivitiAuditDto activitiAuditDto = activitiClient.audit(new ActivitiAuditForm(id, name, auditFile.getProcessInstanceId(), auditFile.getProcessTypeId(), comment, pass, SecurityUtils.getAccountId()));
        AuditFileForm auditFileForm = BeanUtil.map(auditFile, AuditFileForm.class);
        auditFileForm.setLocked(true);
        auditFileForm.setProcessFlowId(activitiAuditDto.getProcessFlowId());
        auditFileForm.setProcessStatus(activitiAuditDto.getProcessStatus());
        auditFileForm.setPositionId(activitiAuditDto.getPositionId());
        auditFileMapper.updateForm(auditFileForm);
        activitiClient.notify(new ActivitiNotifyForm(name,auditFile.getId(),auditFile.getProcessStatus(),SecurityUtils.getPositionId(),SecurityUtils.getCompanyId(),SecurityUtils.getAccountId(),SecurityUtils.getOfficeId()));

    }

    public void logicDeleteOne(String id) {
        auditFileMapper.logicDeleteOne(id);
    }
}
