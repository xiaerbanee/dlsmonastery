package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.DutyOvertime;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyOvertimeForm extends BaseForm<DutyOvertime> {
    private Double hour;
    private Double leftHour;
    private String status;
    private String employeeId;
    private LocalDate dutyDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
}
