package net.myspring.basic.modules.hr.domain;

import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="hr_duty_free")
public class DutyFree extends AuditEntity<DutyFree> {
    private LocalDate freeDate;
    private String dateType;
    private String reason;
    private Integer version = 0;
    private String employeeId;

    public LocalDate getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(LocalDate freeDate) {
        this.freeDate = freeDate;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
