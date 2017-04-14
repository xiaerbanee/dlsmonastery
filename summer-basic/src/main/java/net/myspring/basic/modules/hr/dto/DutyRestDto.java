package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.DutyRest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by admin on 2017/4/5.
 */
public class DutyRestDto extends DataDto<DutyRest> {
    private LocalDate dutyDate;
    private String type;
    private String dateType;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private Double hour;
    private String status;

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Double getHour() {
        return hour;
    }

    public void setHour(Double hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
