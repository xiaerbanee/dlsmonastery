package net.myspring.cloud.modules.report.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.modules.report.dto.ReceivableForDetailDto;
import net.myspring.cloud.modules.report.dto.ReceivableForSummaryDto;
import net.myspring.cloud.modules.report.service.ReceivableReportService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lihx on 2016/12/20.
 */
@RestController
@RequestMapping(value="cloud/receivableReport")
public class ReceivableReportController {
    @Autowired
    private ReceivableReportService receivableReportService;

    @RequestMapping(value="summaryList")
    public String summaryList(Model model, String dateRange, String companyName) {
        List<ReceivableForSummaryDto> receivableReportForSummaryList = Lists.newArrayList();
        LocalDate dateStart = LocalDate.now().minusDays(7L);
        LocalDate dateEnd = LocalDate.now().minusDays(1L);
        if (StringUtils.isNotEmpty(dateRange)) {
            String[] dates = dateRange.split(CharEnum.WAVE_LINE.getValue());
            dateStart = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
            dateEnd = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
        }
        if (StringUtils.isNotBlank(companyName)) {
            receivableReportForSummaryList = receivableReportService.getSummaryList(dateStart, dateEnd);
        }
        model.addAttribute("receivableReportForSummaryList", receivableReportForSummaryList);
        model.addAttribute("dateRange", dateStart + CharEnum.WAVE_LINE.getValue() + dateEnd);
        model.addAttribute("companyName", companyName);
        return "cloud/receivableReportForSummaryList";
    }

    @RequestMapping(value = "detailList")
    public String detailList(Model model,String companyName,String masterId,String dateRange){
        String[] dates = dateRange.split(CharEnum.WAVE_LINE.getValue());
        LocalDate dateStart = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
        LocalDate dateEnd = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
        List<ReceivableForDetailDto> receivableReportForDetailList = Lists.newArrayList();
        if (StringUtils.isNotBlank(companyName)) {
            receivableReportForDetailList = receivableReportService.getDetailList(dateStart, dateEnd,masterId);
        }
        model.addAttribute("receivableReportForDetailList",receivableReportForDetailList);
        return "cloud/receivableReportForDetailList";
    }
}
