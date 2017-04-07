package net.myspring.basic.modules.hr.web.query;

import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyWorktimeQuery {
    private String dutyDate;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        if(!dutyDate.equals("")) {
            String[] dutyDateBetween = dutyDate.split(" - ");
            this.dateStart = LocalDateUtils.parse(dutyDateBetween[0]);
            this.dateEnd = LocalDateUtils.parse(dutyDateBetween[1]);
        }else {
            this.dutyDate = dutyDate;
        }
    }

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
}
