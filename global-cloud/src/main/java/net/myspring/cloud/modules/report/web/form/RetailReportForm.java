package net.myspring.cloud.modules.report.web.form;


import java.util.List;

/**
 * Created by lihx on 2017/5/8.
 */
public class RetailReportForm {
    private List<List<Object>> retailReport;
    private String startMonth;
    private String endMonth;

    public List<List<Object>> getRetailReport() {
        return retailReport;
    }

    public void setRetailReport(List<List<Object>> retailReport) {
        this.retailReport = retailReport;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }
}
