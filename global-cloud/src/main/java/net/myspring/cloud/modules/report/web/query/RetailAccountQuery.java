package net.myspring.cloud.modules.report.web.query;

import java.time.YearMonth;
import java.util.List;

/**
 * Created by lihx on 2017/6/7.
 */
public class RetailAccountQuery {
    private YearMonth monthStart;
    private YearMonth monthEnd;
    private List<String> departmentNumber;

    public YearMonth getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(YearMonth monthStart) {
        this.monthStart = monthStart;
    }

    public YearMonth getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(YearMonth monthEnd) {
        this.monthEnd = monthEnd;
    }

    public List<String> getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(List<String> departmentNumber) {
        this.departmentNumber = departmentNumber;
    }
}
