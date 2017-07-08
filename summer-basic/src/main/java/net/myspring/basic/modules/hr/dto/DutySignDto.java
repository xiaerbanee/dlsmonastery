package net.myspring.basic.modules.hr.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.DutySign;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by admin on 2017/4/5.
 */
public class DutySignDto extends DataDto<DutySign> {
    private String employeeName;
    private LocalDate dutyDate;
    private String week;
    private LocalTime dutyTime;
    private String address;
    private String uuid;
    private String netType;
    private String attachment;
    private String status;

    private boolean deleted=false;

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getWeek() {
        if(StringUtils.isBlank(week)&&dutyDate!=null){
            this.week= LocalDateUtils.getDayOfWeek(dutyDate);
        }
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public LocalTime getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(LocalTime dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
