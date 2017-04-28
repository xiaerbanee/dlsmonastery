package net.myspring.cloud.modules.report.web.form;

import net.myspring.cloud.modules.report.dto.PayableForSummaryDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/28.
 */
public class PayableSummaryReportForm {
    private List<PayableForSummaryDto> payableSummaryList;
    private String dateRange;
    private String companyName;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
