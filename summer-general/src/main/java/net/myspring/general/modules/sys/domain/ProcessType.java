package net.myspring.general.modules.sys.domain;

import net.myspring.general.common.domain.CompanyEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_process_type")
@Where(clause = "enabled=true")
public class ProcessType extends CompanyEntity<ProcessType> {
    private String type;
    private String name;
    private String viewPositionIds;
    private String createdPositionIds;
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

    public String getCreatedPositionIds() {
        return createdPositionIds;
    }

    public void setCreatedPositionIds(String createdPositionIds) {
        this.createdPositionIds = createdPositionIds;
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
