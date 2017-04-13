package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.basic.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */
public class DutyRestForm extends DataForm<DutyRest> {
    private Double annualLeftHour;
    private Double overtimeLeftHour;
    private String status;
    private String employeeId;

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
