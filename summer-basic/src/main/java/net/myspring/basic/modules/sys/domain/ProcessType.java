package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.domain.EmployeeSalaryBasic;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_process_type")
public class ProcessType extends CompanyEntity<ProcessType> {
    private String type;
    private String name;
    private String viewPermissionId;
    private String createPermissionId;
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
