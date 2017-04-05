package net.myspring.basic.modules.hr.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="hr_duty_worktime")
public class DutyWorktime extends DataEntity<DutyWorktime> {
    private String type;
    private LocalDate dutyDate;
    private LocalTime dutyTime;
    private Integer version = 0;
    private List<DutySign> dutySignList = Lists.newArrayList();
    private List<String> dutySignIdList = Lists.newArrayList();
    private Employee employee;
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

    public List<DutySign> getDutySignList() {
        return dutySignList;
    }

    public void setDutySignList(List<DutySign> dutySignList) {
        this.dutySignList = dutySignList;
    }

    public List<String> getDutySignIdList() {
        return dutySignIdList;
    }

    public void setDutySignIdList(List<String> dutySignIdList) {
        this.dutySignIdList = dutySignIdList;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
