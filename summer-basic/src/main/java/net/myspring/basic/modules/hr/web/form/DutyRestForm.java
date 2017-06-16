package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.enums.DutyRestTypeEnum;
import net.myspring.common.form.BaseForm;
import net.myspring.basic.modules.hr.domain.DutyRest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by admin on 2017/4/6.
 */
public class DutyRestForm extends BaseForm<DutyRest> {
    private Double annualLeftHour;
    private Double overtimeLeftHour;
    private String status;
    private String employeeId;
    private Double expiredHour;
    private String type;
    private LocalDate dutyDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String dateType;
    private BigDecimal hour;

    public Double getAnnualLeftHour() {
        return annualLeftHour;
    }

    public void setAnnualLeftHour(Double annualLeftHour) {
        this.annualLeftHour = annualLeftHour;
    }

    public Double getOvertimeLeftHour() {
        return overtimeLeftHour;
    }

    public void setOvertimeLeftHour(Double overtimeLeftHour) {
        this.overtimeLeftHour = overtimeLeftHour;
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

    public Double getExpiredHour() {
        return expiredHour;
    }

    public void setExpiredHour(Double expiredHour) {
        this.expiredHour = expiredHour;
    }

    public String getType() {
        return type;
    }

    public void setType(String restType) {
        this.type = restType;
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
}
