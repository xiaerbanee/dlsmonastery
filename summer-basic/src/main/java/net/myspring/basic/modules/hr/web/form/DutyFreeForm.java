package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyFreeForm implements BaseForm<DutyFree> {
    private String employeeId;
    private String Status;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
