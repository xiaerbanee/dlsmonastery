package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.DutyLeave;
import net.myspring.mybatis.form.BaseForm;

import java.time.LocalDate;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyLeaveForm implements BaseForm<DutyLeave>{
    private String dutyDateStart;
    private String dutyDateEnd;
    private String employeeId;
    private String dateType;
    private LocalDate dutyDate;
    private String status;
    private String leaveType;
    private String attachment;
    private String remarks;

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
