package net.myspring.cloud.modules.report.web.form;

import net.myspring.cloud.modules.report.dto.ReceivableForSummaryDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/28.
 */
public class ReceivableReportForm {
    private List<ReceivableForSummaryDto> receivableSummaryList;
    private String dateRange;
    private String primaryGroupName;

    public String getPrimaryGroupName() {
        return primaryGroupName;
    }

    public void setPrimaryGroupName(String primaryGroupName) {
        this.primaryGroupName = primaryGroupName;
    }

    public List<ReceivableForSummaryDto> getReceivableSummaryList() {
        return receivableSummaryList;
    }

    public void setReceivableSummaryList(List<ReceivableForSummaryDto> receivableSummaryList) {
        this.receivableSummaryList = receivableSummaryList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

}
