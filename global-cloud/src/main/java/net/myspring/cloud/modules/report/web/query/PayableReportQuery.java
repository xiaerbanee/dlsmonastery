package net.myspring.cloud.modules.report.web.query;

import net.myspring.cloud.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/5/2.
 */
public class PayableReportQuery extends BaseQuery {
    private String dateRange;
    private LocalDate startDate;
    private LocalDate endDate;

    //detail
    private String supplierId;
    private String departmentId;

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
            this.startDate = LocalDate.now().minusDays(7L);
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
            this.endDate = LocalDate.now().minusDays(1L);
        }
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
