package net.myspring.cloud.modules.kingdee.web.query;

import net.myspring.cloud.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by liuj on 2017/5/16.
 */
public class BdCustomerQuery extends BaseQuery {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String dateRange;

    public LocalDate getDateStart() {
        if(StringUtils.isNotBlank(dateRange)) {
            return LocalDateUtils.parse(dateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        if(StringUtils.isNotBlank(dateRange)) {
            return LocalDateUtils.parse(dateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}
