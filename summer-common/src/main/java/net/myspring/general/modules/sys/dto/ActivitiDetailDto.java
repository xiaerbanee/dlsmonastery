package net.myspring.general.modules.sys.dto;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/22.
 */
public class ActivitiDetailDto {
    private String processStatus;
    private String auditBy;
    private LocalDateTime auditDate;
    private String comment;

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
