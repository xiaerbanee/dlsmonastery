package net.myspring.cloud.modules.report.web.form;

import net.myspring.cloud.modules.report.dto.PayableForSummaryDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/28.
 */
public class PayableReportForm {
    private List<PayableForSummaryDto> payableSummaryList;
    private String dateRange;

    public List<PayableForSummaryDto> getPayableSummaryList() {
        return payableSummaryList;
    }

    public void setPayableSummaryList(List<PayableForSummaryDto> payableSummaryList) {
        this.payableSummaryList = payableSummaryList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}
