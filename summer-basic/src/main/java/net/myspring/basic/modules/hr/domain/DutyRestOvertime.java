package net.myspring.basic.modules.hr.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_duty_rest_overtime")
public class DutyRestOvertime extends IdEntity<DutyRestOvertime> {
    private String restId;
    private String overtimeId;
    private Double restHour;

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getOvertimeId() {
        return overtimeId;
    }

    public void setOvertimeId(String overtimeId) {
        this.overtimeId = overtimeId;
    }

    public Double getRestHour() {
        return restHour;
    }

    public void setRestHour(Double restHour) {
        this.restHour = restHour;
    }
}
