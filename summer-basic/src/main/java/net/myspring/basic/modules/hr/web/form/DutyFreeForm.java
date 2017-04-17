package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyFreeForm extends DataForm<DutyFree> {
    private String employeeId;
    private String Status;
    private DutyDateTypeEnum[] dateList;

    public DutyDateTypeEnum[] getDateList() {
        return dateList;
    }

    public void setDateList(DutyDateTypeEnum[] dateList) {
        this.dateList = dateList;
    }

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
