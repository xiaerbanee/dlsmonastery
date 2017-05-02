package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutyTrip;
import net.myspring.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyTripForm extends DataForm<DutyTrip> {
    private String status;
    private String employeeId;

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
