package net.myspring.cloud.modules.report.web.form;

import net.myspring.cloud.modules.report.dto.ReceivableForSummaryDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/28.
 */
public class ReceivableSummaryReportForm {
    private List<ReceivableForSummaryDto> receivableForSummaryList;
    private String dateRange;
    private String companyName;

    public List<ReceivableForSummaryDto> getReceivableForSummaryList() {
        return receivableForSummaryList;
    }

    public void setReceivableForSummaryList(List<ReceivableForSummaryDto> receivableForSummaryList) {
        this.receivableForSummaryList = receivableForSummaryList;
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
