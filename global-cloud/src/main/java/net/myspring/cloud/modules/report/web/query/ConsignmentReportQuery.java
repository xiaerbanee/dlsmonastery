package net.myspring.cloud.modules.report.web.query;

import net.myspring.cloud.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/5/5.
 */
public class ConsignmentReportQuery extends BaseQuery {
    private String dateRange;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public LocalDate getStartDate() {
        if(StringUtils.isNotBlank(dateRange)){
            String[] tempParamValues = dateRange.split(CharConstant.DATE_RANGE_SPLITTER);
            this.startDate = LocalDateUtils.parse(tempParamValues[0]);
        }else{
            this.startDate = LocalDate.now().minusWeeks(1);
        }
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        if(StringUtils.isNotBlank(dateRange)){
            String[] tempParamValues = dateRange.split(CharConstant.DATE_RANGE_SPLITTER);
            this.endDate = LocalDateUtils.parse(tempParamValues[1]);
        }else{
            this.endDate = LocalDate.now().minusDays(1);
        }
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
