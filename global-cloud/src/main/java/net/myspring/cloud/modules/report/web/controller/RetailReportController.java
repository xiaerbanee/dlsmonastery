package net.myspring.cloud.modules.report.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.common.handsontable.NestedHeaderCell;
import net.myspring.cloud.modules.report.domain.Retail;
import net.myspring.cloud.modules.report.service.RetailReportForAssistService;
import net.myspring.cloud.modules.report.service.RetailReportService;
import net.myspring.cloud.modules.report.web.form.RetailReportForm;
import net.myspring.cloud.modules.report.web.query.RetailReportQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by lihx on 2017/2/10.
 */
@RestController
@RequestMapping(value = "report/retailReport17")
public class RetailReportController {
    @Autowired
    private RetailReportForAssistService retailReportForAssistService;
    @Autowired
    private RetailReportService retailReportService;


    @RequestMapping(value = "report")
    public RetailReportForm report(RetailReportQuery retailReportQuery) {
        YearMonth start = retailReportQuery.getStartDate();
        YearMonth end = retailReportQuery.getEndDate();
        List<List<NestedHeaderCell>> nestedHeader = retailReportForAssistService.getNestedHeads(start, end);
        List<List<Object>> retailShopReport = retailReportService.getRetailReport(start, end);
        RetailReportForm retailReportForm = new RetailReportForm();
        retailReportForm.setStartMonth(start.format(DateTimeFormatter.ofPattern(DateFormat.MONTH.getValue())));
        retailReportForm.setEndMonth(end.format(DateTimeFormatter.ofPattern(DateFormat.MONTH.getValue())));
        retailReportForm.setRetailReport(retailShopReport);
        retailReportForm.setNestedHeader(nestedHeader);;
        return retailReportForm;
    }

    @RequestMapping(value = "export")
    public ModelAndView export(String monthStart, String monthEnd, String companyName) {
        List<List<Object>> ReportDataList = Lists.newArrayList();
        List<Retail> ReportDataForAssistList = Lists.newArrayList();
        YearMonth start = YearMonth.now().minusMonths(3L);
        YearMonth end = YearMonth.now().minusMonths(1L);
        if (StringUtils.isNotBlank(monthStart)) {start = YearMonth.parse(monthStart);}
        if (StringUtils.isNotBlank(monthEnd)) { end = YearMonth.parse(monthEnd);}
        if (StringUtils.isNotBlank(companyName)) {
            ReportDataList = retailReportForAssistService.getRetailReportHead(start, end);
            ReportDataForAssistList.addAll(retailReportService.getRetailReport2(start, end));
            ReportDataList.addAll(retailReportService.getRetailReport(start, end));
        }
        return null;
    }
}
