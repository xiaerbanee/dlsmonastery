package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */
public class DutyRestForm implements BaseForm<DutyRest> {
    private String id;
    private Double annualLeftHour;
    private Double overtimeLeftHour;
    private String status;
    private String employeeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
