package net.myspring.cloud.modules.report.web.form;

import net.myspring.cloud.modules.report.dto.PayableForDetailDto;
import net.myspring.cloud.modules.report.dto.PayableForSummaryDto;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * Created by lihx on 2017/4/28.
 */
public class PayableReportForm {
    private List<PayableForSummaryDto> payableSummaryList;
    private String dateRange;
    //detail
    private List<PayableForDetailDto> payableDetailList;

    public List<PayableForDetailDto> getPayableDetailList() {
        return payableDetailList;
    }

    public void setPayableDetailList(List<PayableForDetailDto> payableDetailList) {
        this.payableDetailList = payableDetailList;
    }

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
