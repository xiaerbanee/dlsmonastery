package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.DutyTrip;

import java.time.LocalDate;

/**
 * Created by admin on 2017/4/5.
 */
public class DutyTripDto extends DataDto<DutyTrip> {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
