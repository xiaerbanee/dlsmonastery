package net.myspring.general.modules.sys.dto;


import net.myspring.general.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.ProcessType;

/**
 * Created by admin on 2017/4/5.
 */
public class ProcessTypeDto extends DataDto<ProcessType> {
    private String type;
    private String name;
    private String viewPermissionId;
    private String createPermissionId;
    private Boolean auditFileType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewPermissionId() {
        return viewPermissionId;
    }

    public void setViewPermissionId(String viewPermissionId) {
        this.viewPermissionId = viewPermissionId;
    }

    public String getCreatePermissionId() {
        return createPermissionId;
    }

    public void setCreatePermissionId(String createPermissionId) {
        this.createPermissionId = createPermissionId;
    }

    public Boolean getAuditFileType() {
        return auditFileType;
    }

    public void setAuditFileType(Boolean auditFileType) {
        this.auditFileType = auditFileType;
    }
}
