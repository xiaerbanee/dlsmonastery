package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyWorktimeQuery extends BaseQuery {
    private String dutyDate;
    private String accountId;
    private String officeId;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDate getDutyDateStart() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getDutyDateEnd() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}
