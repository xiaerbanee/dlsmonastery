package net.myspring.basic.modules.hr.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.DutyFree;

import java.time.LocalDate;

/**
 * Created by admin on 2017/4/5.
 */
public class DutyFreeDto extends DataDto<DutyFree> {
    private LocalDate freeDate;
    private String dateType;
    private String status;

    public LocalDate getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(LocalDate freeDate) {
        this.freeDate = freeDate;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
