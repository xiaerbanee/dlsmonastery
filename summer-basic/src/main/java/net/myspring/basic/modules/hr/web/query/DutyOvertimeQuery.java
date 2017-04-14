package net.myspring.basic.modules.hr.web.query;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyOvertimeQuery {
    private String createdBy;
    private LocalDate dutyDateStart;
    private LocalDate dutyDateEnd;
    private String officeIds;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getDutyDateStart() {
        return dutyDateStart;
    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public LocalDate getDutyDateEnd() {
        return dutyDateEnd;
    }

    public void setDutyDateEnd(LocalDate dutyDateEnd) {
        this.dutyDateEnd = dutyDateEnd;
    }

    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }
}
