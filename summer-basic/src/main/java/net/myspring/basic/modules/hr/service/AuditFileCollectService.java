package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.AuditFileCollect;
import net.myspring.basic.modules.hr.repository.AuditFileCollectRepository;
import net.myspring.basic.modules.hr.repository.AuditFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class AuditFileCollectService {

    @Autowired
    private AuditFileCollectRepository auditFileCollectRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AuditFileRepository auditFileRepository;

    @Transactional
    public void collect(String auditFileId, boolean collect) {
        String accountId = RequestUtils.getAccountId();
        AuditFileCollect auditFileCollect = auditFileCollectRepository.findByAccountIdAndAuditFileIdAndEnabledIsTrue(accountId, auditFileId);
        if (auditFileCollect == null && collect) {
            auditFileCollect = new AuditFileCollect();
            auditFileCollect.setAccountId(accountId);
            auditFileCollect.setAuditFileId(auditFileId);
            auditFileCollect.setCollectDate(LocalDate.now());
            auditFileCollectRepository.save(auditFileCollect);
        } else if (auditFileCollect != null && !collect) {
            auditFileCollect.setEnabled(false);
            auditFileCollectRepository.save(auditFileCollect);
}
    }

}
