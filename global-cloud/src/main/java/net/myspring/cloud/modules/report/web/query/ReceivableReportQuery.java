package net.myspring.cloud.modules.report.web.query;

import net.myspring.cloud.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/5/3.
 */
public class ReceivableReportQuery extends BaseQuery {
    private String dateRangeBTW;
    private LocalDate startDate;
    private LocalDate endDate;
    //summary
    private String primaryGroupId;
    //detail
    private String customerId;
    //export
    private List<String> primaryGroupIds;

    public String getDateRangeBTW() {
        return dateRangeBTW;
    }

    public void setDateRangeBTW(String dateRangeBTW) {
        this.dateRangeBTW = dateRangeBTW;
    }

    public LocalDate getStartDate() {
        if(StringUtils.isNotBlank(dateRangeBTW)){
            String[] tempParamValues = dateRangeBTW.split(CharConstant.DATE_RANGE_SPLITTER);
            this.startDate = LocalDateUtils.parse(tempParamValues[0]);
        }else{
            this.startDate = LocalDate.now().minusDays(3L);
        }
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        if(StringUtils.isNotBlank(dateRangeBTW)){
            String[] tempParamValues = dateRangeBTW.split(CharConstant.DATE_RANGE_SPLITTER);
            this.endDate = LocalDateUtils.parse(tempParamValues[1]);
        }else{
            this.endDate = LocalDate.now().minusDays(1L);
        }
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPrimaryGroupId() {
        return primaryGroupId;
    }

    public void setPrimaryGroupId(String primaryGroupId) {
        this.primaryGroupId = primaryGroupId;
    }

    public List<String> getPrimaryGroupIds() {
        return primaryGroupIds;
    }

    public void setPrimaryGroupIds(List<String> primaryGroupIds) {
        this.primaryGroupIds = primaryGroupIds;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
