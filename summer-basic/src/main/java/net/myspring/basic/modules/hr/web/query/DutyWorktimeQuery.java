package net.myspring.basic.modules.hr.web.query;

import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyWorktimeQuery {
    private String dutyDate;
    private LocalDate dutyDateStart;
    private LocalDate dutyDateEnd;
    private String accountId;

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        if(!dutyDate.equals("")) {
            String[] dutyDateBetween = dutyDate.split(" - ");
            this.dutyDateStart = LocalDateUtils.parse(dutyDateBetween[0]);
            this.dutyDateEnd = LocalDateUtils.parse(dutyDateBetween[1]);
        }else {
            this.dutyDate = dutyDate;
        }
    }

    public LocalDate getDutyDateStart() {
        return dutyDateStart;
    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public LocalDate getDutyDateEnd() {
        return dutyDateEnd;
    }

    public void setDutyDateEnd(LocalDate dutyDateEnd) {
        this.dutyDateEnd = dutyDateEnd;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
