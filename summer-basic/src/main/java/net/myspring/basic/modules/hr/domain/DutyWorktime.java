package net.myspring.basic.modules.hr.domain;


import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="hr_duty_worktime")
public class DutyWorktime extends DataEntity<DutyWorktime> {
    private String type;
    private LocalDate dutyDate;
    private LocalTime dutyTime;
    private Integer version = 0;
    private String employeeId;

    private String dutyTimeStart;
    private String dutyTimeEnd;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDutyTimeStart() {
        return dutyTimeStart;
    }

    public void setDutyTimeStart(String dutyTimeStart) {
        this.dutyTimeStart = dutyTimeStart;
    }

    public String getDutyTimeEnd() {
        return dutyTimeEnd;
    }

    public void setDutyTimeEnd(String dutyTimeEnd) {
        this.dutyTimeEnd = dutyTimeEnd;
    }
}
