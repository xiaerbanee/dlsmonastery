package net.myspring.basic.modules.hr.service;

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
import net.myspring.general.modules.sys.dto.ActivitiAuthenticatedDto;
import net.myspring.general.modules.sys.form.ActivitiAuthenticatedForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            String businessKey = auditFileForm.getId();
            ActivitiAuthenticatedDto authenticated = activitiClient.authenticated(new ActivitiAuthenticatedForm(auditFileForm.getId(),"文件审批",businessKey,auditFileForm.getProcessTypeId(), SecurityUtils.getAccountId()));
            auditFileForm.setProcessStatus(authenticated.getProcessStatus());
            auditFileForm.setProcessFlowId(authenticated.getProcessFlowId());
            auditFileForm.setPositionId(authenticated.getPositionId());
            auditFile=BeanUtil.map(auditFileForm,AuditFile.class);
            auditFileMapper.save(auditFile);
            return auditFile;
        }
        return null;
    }

    public void logicDeleteOne(String id) {
        auditFileMapper.logicDeleteOne(id);
    }
}
