package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.DutyOvertime;
import net.myspring.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyOvertimeForm extends DataForm<DutyOvertime> {
    private Integer hour;
    private Integer leftHour;
    private String status;
    private String employeeId;

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getLeftHour() {
        return leftHour;
    }

    public void setLeftHour(Integer leftHour) {
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
}
