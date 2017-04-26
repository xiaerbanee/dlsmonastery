package net.myspring.basic.modules.hr.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.basic.modules.sys.domain.Permission;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name="hr_position")
public class Position extends CompanyEntity<Position> {
    private String name;
    private Integer version = 0;
    private String permission;
    private Integer dataScope;
    private Integer secretLevel;
    private String reportName;
    private Integer sort;
    private String type;
    private String jobId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(Integer secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}
