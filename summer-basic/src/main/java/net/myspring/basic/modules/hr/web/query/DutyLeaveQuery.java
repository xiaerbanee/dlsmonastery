package net.myspring.basic.modules.hr.web.query;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyLeaveQuery {
    private String createdBy;
    private LocalDate dutyDateStart;
    private LocalDate dutyDateEnd;
    private String dateType;
    private String leaveType;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}
