package net.myspring.basic.modules.hr.domain;

import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="hr_audit_file_collect")
public class AuditFileCollect extends DataEntity<AuditFileCollect>{
    private String accountId;
    private String auditFileId;
    private LocalDate collectDate;
    private String accountFavoriteId;

    public String getAccountFavoriteId() {
        return accountFavoriteId;
    }

    public void setAccountFavoriteId(String accountFavoriteId) {
        this.accountFavoriteId = accountFavoriteId;
    }

    public LocalDate getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(LocalDate collectDate) {
        this.collectDate = collectDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAuditFileId() {
        return auditFileId;
    }

    public void setAuditFileId(String auditFileId) {
        this.auditFileId = auditFileId;
    }
}
