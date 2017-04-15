package net.myspring.basic.modules.hr.web.query;

import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
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
        this.dutyDate = dutyDate;
    }

    public LocalDate getDutyDateStart() {
        if(dutyDateStart==null&& StringUtils.isNotBlank(dutyDate)){
            String[] tempParamValues = dutyDate.split(" - ");
            this.dutyDateStart= LocalDateUtils.parse(tempParamValues[0]);
        }
        return dutyDateStart;
    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public LocalDate getDutyDateEnd() {
        if(dutyDateEnd==null&& StringUtils.isNotBlank(dutyDate)){
            String[] tempParamValues = dutyDate.split(" - ");
            this.dutyDateEnd= LocalDateUtils.parse(tempParamValues[1]);
        }
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
