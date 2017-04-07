package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutySign;
import net.myspring.mybatis.form.BaseForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by admin on 2017/4/6.
 */

public class DutySignForm extends BaseForm<DutySign> {
    private LocalDate dutyDate;
    private LocalTime dutyTime;
    private String status;
    private String EmployeeId;

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public LocalTime getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(LocalTime dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }
}
