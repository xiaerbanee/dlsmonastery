package net.myspring.basic.modules.hr.domain;

import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;

@Entity
@Table(name="hr_duty_leave")
public class DutyLeave extends AuditEntity<DutyLeave> {
    private String leaveType;
    private LocalDate dutyDate;
    private String dateType;
    private String attachment;
    private Integer version = 0;
    private Employee employee;
    private String employeeId;

    @Transient
    private String dutyDateStart;
    @Transient
    private String dutyDateEnd;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDutyDateStart() {
        return dutyDateStart;
    }

    public void setDutyDateStart(String dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public String getDutyDateEnd() {
        return dutyDateEnd;
    }

    public void setDutyDateEnd(String dutyDateEnd) {
        this.dutyDateEnd = dutyDateEnd;
    }
}
