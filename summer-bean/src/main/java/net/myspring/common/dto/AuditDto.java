package net.myspring.common.dto;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/3/31.
 */
public class AuditDto<T> extends DataDto<T> {
    protected String auditBy;
    protected LocalDateTime auditDate;
    protected String auditRemarks;
    protected String status;

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public LocalDateTime getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDateTime auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
