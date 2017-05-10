package net.myspring.cloud.modules.report.web.query;


import net.myspring.cloud.common.query.BaseQuery;

import java.time.YearMonth;
import java.util.List;

/**
 * Created by lihx on 2017/5/8.
 */
public class RetailReportQuery extends BaseQuery {
    private YearMonth startMonth;
    private YearMonth endMonth;
    private List<String> departmentNumber;

    public YearMonth getStartMonth() {
        if(startMonth == null){
            startMonth = YearMonth.now().minusMonths(3L);
        }
        return startMonth;
    }

    public void setStartMonth(YearMonth startMonth) {
        this.startMonth = startMonth;
    }

    public YearMonth getEndMonth() {
        if(endMonth == null){
            endMonth = YearMonth.now().minusMonths(1L);
        }
        return endMonth;
    }

    public void setEndMonth(YearMonth endMonth) {
        this.endMonth = endMonth;
    }

    public List<String> getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(List<String> departmentNumber) {
        this.departmentNumber = departmentNumber;
    }
}
