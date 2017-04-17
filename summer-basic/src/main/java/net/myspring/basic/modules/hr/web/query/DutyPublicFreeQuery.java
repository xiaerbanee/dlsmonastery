package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.enums.DutyDateTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyPublicFreeQuery {
    private LocalDate dutyDateStart;
    private LocalDate dutyDateEnd;
    private String employeeName;
    private List<String> officeIds;
    private String createdBy;
    private DutyDateTypeEnum[] dateList;

    public DutyDateTypeEnum[] getDateList() {
        return dateList;
    }

    public void setDateList(DutyDateTypeEnum[] dateList) {
        this.dateList = dateList;
    }

    public LocalDate getDutyDateStart() {
        return dutyDateStart;
    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public LocalDate getDutyDateEnd() {
        return dutyDateEnd;
    }

    public void setDutyDateEnd(LocalDate dutyDateEnd) {
        this.dutyDateEnd = dutyDateEnd;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
