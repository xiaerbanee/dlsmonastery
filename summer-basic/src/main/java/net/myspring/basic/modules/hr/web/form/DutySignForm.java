package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutySign;
import net.myspring.common.form.BaseForm;
import net.myspring.util.text.StringUtils;

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
    private LocalDateTime dutyDateTime;
    private String address;
    private String attachment;
    private String longitude;
    private String latitude;
    private String networkType;
    private String model;
    private String accuracy;

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

    public void setDutyDateTime(LocalDateTime dutyDateTime) {
        this.dutyDateTime = dutyDateTime;
    }

    public LocalDateTime getDutyDateTime() {
        if(dutyDate!=null&&dutyTime!=null){
            this.dutyDateTime= LocalDateTime.of(dutyDate,dutyTime);
        }
        return dutyDateTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
