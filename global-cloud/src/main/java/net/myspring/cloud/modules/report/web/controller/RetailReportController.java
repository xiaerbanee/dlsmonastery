package net.myspring.cloud.modules.report.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.KingdeeTypeEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.report.domain.Retail;
import net.myspring.cloud.modules.report.service.GlcxViewService;
import net.myspring.cloud.modules.report.service.RetailReportForAssistService;
import net.myspring.cloud.modules.report.service.RetailReportService;
import net.myspring.cloud.modules.report.web.form.RetailReportForm;
import net.myspring.cloud.modules.report.web.query.RetailReportQuery;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.YearMonthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.YearMonth;
import java.util.List;

/**
 * Created by lihx on 2017/2/10.
 */
@RestController
@RequestMapping(value = "report/retailReport")
public class RetailReportController {
    @Autowired
    private RetailReportForAssistService retailReportForAssistService;
    @Autowired
    private RetailReportService retailReportService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private GlcxViewService glcxViewService;

    @RequestMapping(value = "report")
    public RetailReportForm report(RetailReportQuery retailReportQuery) {
        RetailReportForm retailReportForm = new RetailReportForm();
        YearMonth start = retailReportQuery.getStartMonth();
        YearMonth end = retailReportQuery.getEndMonth();
        List<String> departmentNumberList = retailReportQuery.getDepartmentNumber();
        String type = kingdeeBookService.getTypeByCompanyId(RequestUtils.getCompanyId());
        if (KingdeeTypeEnum.retail.name().equals(type)){
            if (retailReportQuery.getDepartmentNumber() == null){
                 departmentNumberList = glcxViewService.findDefaultDepartment();
                retailReportQuery.setDepartmentNumber(departmentNumberList);
            }
            List<List<Object>> retailShopReport = retailReportService.getRetailReport(start, end,departmentNumberList);
            retailReportForm.setStartMonth(YearMonthUtils.format(start));
            retailReportForm.setEndMonth(YearMonthUtils.format(end));
            retailReportForm.setRetailReport(retailShopReport);
        }else{
            retailReportForm.setStartMonth("该报表仅限于零售账套");
        }
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
            ReportDataList.addAll(retailReportService.getRetailReport(start, end,null));
        }
        return null;
    }
}
