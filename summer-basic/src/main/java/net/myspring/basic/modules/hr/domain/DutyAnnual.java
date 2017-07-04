package net.myspring.basic.modules.hr.domain;


import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_duty_annual")
public class DutyAnnual extends DataEntity<DutyAnnual> {
    private String annualYear;
    private Double hour;
    private Double leftHour;
    private Integer version = 0;
    private String employeeId;

    public String getAnnualYear() {
        return annualYear;
    }

    public void setAnnualYear(String annualYear) {
        this.annualYear = annualYear;
    }

    public Double getHour() {
        return hour;
    }

    public void setHour(Double hour) {
        this.hour = hour;
    }

    public Double getLeftHour() {
        return leftHour;
    }

    public void setLeftHour(Double leftHour) {
        this.leftHour = leftHour;
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
