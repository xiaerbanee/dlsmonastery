package net.myspring.basic.modules.hr.web.query;

/**
 * Created by lihx on 2017/4/7.
 */
public class AuditFileQuery {

    private String positionId;
    private String auditType;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        if(auditType == null || auditType.equals("1")) {
            this.positionId = positionId;
        }else {
           this.positionId="";
        }
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }
}
