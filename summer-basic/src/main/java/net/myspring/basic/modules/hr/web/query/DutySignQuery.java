package net.myspring.basic.modules.hr.web.query;

import org.joda.time.LocalDateTime;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutySignQuery {
    private LocalDateTime dutyDateStart;
    private LocalDateTime dutyDateEnd;

    public LocalDateTime getDutyDateStart() {
        return dutyDateStart;
    }

    public void setDutyDateStart(LocalDateTime dutyDateStart) {
        if(dutyDateStart == null) {
            this.dutyDateStart = LocalDateTime.now().minusMonths(1);
        }else{
            this.dutyDateStart = dutyDateStart;
        }
    }

    public LocalDateTime getDutyDateEnd() {
        return dutyDateEnd;
    }

    public void setDutyDateEnd(LocalDateTime dutyDateEnd) {
        if(dutyDateEnd == null){
            this.dutyDateEnd = LocalDateTime.now();
        }else{
            this.dutyDateEnd = dutyDateEnd;
        }
    }
}
