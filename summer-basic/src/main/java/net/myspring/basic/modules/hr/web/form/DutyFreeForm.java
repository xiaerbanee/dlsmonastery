package net.myspring.basic.modules.hr.web.form;


import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.common.form.DataForm;

import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyFreeForm extends DataForm<DutyFree> {
    private String employeeId;
    private String Status;
    private List<String> dateList= Lists.newArrayList();

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
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
