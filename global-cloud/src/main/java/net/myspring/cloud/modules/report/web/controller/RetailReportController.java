package net.myspring.cloud.modules.report.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.common.handsontable.NestedHeaderCell;
import net.myspring.cloud.modules.report.domain.Retail;
import net.myspring.cloud.modules.report.service.RetailReportForAssistService;
import net.myspring.cloud.modules.report.service.RetailReportService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by lihx on 2017/2/10.
 */
@Controller
@RequestMapping(value = "report/retailReport17")
public class RetailReportController {
    @Autowired
    private RetailReportForAssistService retailReportForAssistService;
    @Autowired
    private RetailReportService retailReportService;


    @RequestMapping(value = "list")
    public String list(Model model, String monthStart, String monthEnd, String companyName) {
        List<List<Object>> retailShopReport = Lists.newArrayList();
        List<List<NestedHeaderCell>> nestedHeader = Lists.newArrayList();
        YearMonth start = YearMonth.now().minusMonths(3L);
        YearMonth end = YearMonth.now().minusMonths(1L);
        if (StringUtils.isNotBlank(monthStart)) {
            start = YearMonth.parse(monthStart);
        }
        if (StringUtils.isNotBlank(monthEnd)) {
            end = YearMonth.parse(monthEnd);
        }
        if (StringUtils.isNotBlank(companyName)) {
            nestedHeader = retailReportForAssistService.getNestedHeads(start, end);
            retailShopReport = retailReportService.getRetailReport(start, end);
        }
        model.addAttribute("monthStart", start.format(DateTimeFormatter.ofPattern(DateFormat.MONTH.getValue())));
        model.addAttribute("monthEnd", end.format(DateTimeFormatter.ofPattern(DateFormat.MONTH.getValue())));
        model.addAttribute("companyName", companyName);
        model.addAttribute("retailShopReport", retailShopReport);
        model.addAttribute("nestedHeader", nestedHeader);
        return "cloud/retailReport17";
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
