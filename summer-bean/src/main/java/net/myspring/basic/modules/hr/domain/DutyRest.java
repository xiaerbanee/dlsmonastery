package net.myspring.basic.modules.hr.domain;

import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="hr_duty_rest")
public class DutyRest extends AuditEntity<DutyRest> {
    private String type;
    private LocalDate dutyDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String dateType;
    private BigDecimal hour;
    private Integer version = 0;
    private Employee employee;
    private String employeeId;
    private DutyAnnual dutyAnnual;
    private String dutyAnnualId;

    @Transient
    private Double overtimeLeftHour;
    @Transient
    private Double annualLeftHour;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public BigDecimal getHour() {
        return hour;
    }

    public void setHour(BigDecimal hour) {
        this.hour = hour;
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

    public DutyAnnual getDutyAnnual() {
        return dutyAnnual;
    }

    public void setDutyAnnual(DutyAnnual dutyAnnual) {
        this.dutyAnnual = dutyAnnual;
    }

    public String getDutyAnnualId() {
        return dutyAnnualId;
    }

    public void setDutyAnnualId(String dutyAnnualId) {
        this.dutyAnnualId = dutyAnnualId;
    }

    public Double getOvertimeLeftHour() {
        return overtimeLeftHour;
    }

    public void setOvertimeLeftHour(Double overtimeLeftHour) {
        this.overtimeLeftHour = overtimeLeftHour;
    }

    public Double getAnnualLeftHour() {
        return annualLeftHour;
    }

    public void setAnnualLeftHour(Double annualLeftHour) {
        this.annualLeftHour = annualLeftHour;
    }
}
