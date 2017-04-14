package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.DutyAnnual;

import java.time.LocalDate;

/**
 * Created by admin on 2017/4/5.
 */
public class DutyAnnualDto extends DataDto<DutyAnnual> {
    private String officeId;
    private String officeName;
    private String positionId;
    private String positionName;
    private String employeeName;
    private String annualYear;
    private Double hour;
    private Double leftHour;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

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

}
