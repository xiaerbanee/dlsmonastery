package net.myspring.basic.modules.hr.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="hr_duty_annual")
public class DutyAnnual extends CompanyEntity<DutyAnnual> {
    private String annualYear;
    private Double hour;
    private Double leftHour;
    private Integer version = 0;
    private Employee employee;
    private String employeeId;
    private List<DutyRest> dutyRestList = Lists.newArrayList();
    private List<String> dutyRestIdList = Lists.newArrayList();

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

    public List<DutyRest> getDutyRestList() {
        return dutyRestList;
    }

    public void setDutyRestList(List<DutyRest> dutyRestList) {
        this.dutyRestList = dutyRestList;
    }

    public List<String> getDutyRestIdList() {
        return dutyRestIdList;
    }

    public void setDutyRestIdList(List<String> dutyRestIdList) {
        this.dutyRestIdList = dutyRestIdList;
    }
}
