package net.myspring.cloud.modules.report.web.query;


import java.time.YearMonth;

/**
 * Created by lihx on 2017/5/8.
 */
public class RetailReportQuery {
    private YearMonth startMonth;
    private YearMonth endMonth;
    

    public YearMonth getStartDate() {
        if(startMonth == null){
            return this.startMonth = YearMonth.now().minusMonths(3L);
        }
        return startMonth;
    }

    public void setStartDate(YearMonth startMonth) {
        this.startMonth = startMonth;
    }

    public YearMonth getEndDate() {
        if(endMonth == null){
            this.endMonth = YearMonth.now().minusMonths(1L);
        }
        return endMonth;
    }

    public void setEndDate(YearMonth endMonth) {
        this.endMonth = endMonth;
    }
}
