package net.myspring.cloud.modules.report.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.modules.report.dto.PayableForDetailDto;
import net.myspring.cloud.modules.report.dto.PayableForSummaryDto;
import net.myspring.cloud.modules.report.service.PayableReportService;
import net.myspring.cloud.modules.report.web.form.PayableReportForm;
import net.myspring.cloud.modules.report.web.query.PayableReportQuery;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "report/payableReport")
public class PayableReportController {
    @Autowired
    private PayableReportService payableReportService;
    @Autowired
    private KingdeeBookService kingdeeBookService;

    @RequestMapping(value = "summaryList")
    public PayableReportForm summaryList(PayableReportQuery payableReportQuery){
        LocalDate dateStart = payableReportQuery.getStartDate();
        LocalDate dateEnd = payableReportQuery.getEndDate();
        List<PayableForSummaryDto> payableSummaryList = payableReportService.getSummaryList(dateStart, dateEnd);
        PayableReportForm form = new PayableReportForm();
        form.setPayableSummaryList(payableSummaryList);
        form.setDateRange(dateStart + " - " + dateEnd);
        return form;
    }

    @RequestMapping(value = "detailList")
    public PayableReportForm detailList(PayableReportQuery payableReportQuery){
        LocalDate dateStart = payableReportQuery.getStartDate();
        LocalDate dateEnd = payableReportQuery.getEndDate();
        String supplyId = payableReportQuery.getSupplierId();
        String departmentId = payableReportQuery.getDepartmentId();
        List<PayableForDetailDto> payableForDetailList  = payableReportService.getDetailList(dateStart, dateEnd,supplyId,departmentId);
        PayableReportForm form = new PayableReportForm();
        form.setPayableDetailList(payableForDetailList);
        return form;
    }

    @RequestMapping(value = "exportSummary")
    public ModelAndView exportSummary(String dateRange,String companyName,String supplyIdList){
        String[] supplyIds = supplyIdList.split(CharEnum.COMMA.getValue());
        List<PayableForSummaryDto> summaryDataList = Lists.newArrayList();
        List<PayableForSummaryDto> exportSummaryList = Lists.newArrayList();
        LocalDate dateStart = LocalDate.now().minusDays(7L);
        LocalDate dateEnd = LocalDate.now().minusDays(1L);
        if (StringUtils.isNotEmpty(dateRange)) {
            String[] dates = dateRange.split(CharEnum.WAVE_LINE.getValue());
            dateStart = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
            dateEnd = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
        }
        if (StringUtils.isNotBlank(companyName)) {
            summaryDataList = payableReportService.getSummaryList(dateStart, dateEnd);
        }
        for(String supplyId : supplyIds){
            for(PayableForSummaryDto summaryData : summaryDataList){
                if(supplyId.equals(summaryData.getSupplierId())){
                    exportSummaryList.add(summaryData);
                }
            }
        }
        return null;
    }
}
