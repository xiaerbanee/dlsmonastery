package net.myspring.general.modules.sys.domain;

import net.myspring.general.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_process_type")
public class ProcessType extends CompanyEntity<ProcessType> {
    private String type;
    private String name;
    private String viewPositionIds;
    private String createPositionIds;
    private Integer version = 0;
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

    public String getViewPositionIds() {
        return viewPositionIds;
    }

    public void setViewPositionIds(String viewPositionIds) {
        this.viewPositionIds = viewPositionIds;
    }

    public String getCreatePositionIds() {
        return createPositionIds;
    }

    public void setCreatePositionIds(String createPositionIds) {
        this.createPositionIds = createPositionIds;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getAuditFileType() {
        return auditFileType;
    }

    public void setAuditFileType(Boolean auditFileType) {
        this.auditFileType = auditFileType;
    }
}
