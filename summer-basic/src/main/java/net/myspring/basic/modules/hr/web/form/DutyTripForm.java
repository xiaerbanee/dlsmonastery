package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutyTrip;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyTripForm extends BaseForm<DutyTrip> {
    private String status;
    private String employeeId;
    private LocalDate dateStart;
    private LocalDate dateEnd;

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

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }
}
