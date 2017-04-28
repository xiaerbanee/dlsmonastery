package net.myspring.cloud.modules.report.web;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.ConsignmentReportHeadEnum;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.modules.report.dto.ConsignmentForShowDto;
import net.myspring.cloud.modules.report.service.ConsignmentReportService;
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
@RequestMapping(value = "cloud/consignmentReport")
public class ConsignmentReportController {
    @Autowired
    private ConsignmentReportService consignmentReportService;

    @RequestMapping(value = "report")
    public String report(Model model, String dateRange, String companyName) {
        List<ConsignmentForShowDto> consignmentReport = Lists.newArrayList();
        LocalDate dateStart = LocalDate.now().minusWeeks(1);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        if (StringUtils.isNotBlank(dateRange)) {
            String[] dates = dateRange.split(CharEnum.WAVE_LINE.getValue());
            dateStart = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
            dateEnd = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
        }
        //暂仅限于WZOPPO
        if (KingdeeNameEnum.WZOPPO.name().equals(companyName) || KingdeeNameEnum.WZOPPO2016.name().equals(companyName)) {
            consignmentReport = consignmentReportService.findConsignmentReport(dateStart, dateEnd);
        }
        model.addAttribute("heads", ConsignmentReportHeadEnum.getNames());
        model.addAttribute("dateRange", dateStart + CharEnum.WAVE_LINE.getValue() + dateEnd);
        model.addAttribute("consignmentReport", consignmentReport);
        model.addAttribute("companyName", companyName);
        return "cloud/consignmentReport";
    }

    @RequestMapping(value = "export")
    public ModelAndView export(String dateRange, String companyName) {
        List<ConsignmentForShowDto> body = Lists.newArrayList();
        LocalDate dateStart = LocalDate.now().minusWeeks(1);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        if (StringUtils.isNotBlank(dateRange)) {
            String[] dates = dateRange.split(CharEnum.WAVE_LINE.getValue());
            dateStart = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
            dateEnd = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(DateFormat.DATE.getValue()));
        }
        //暂仅限于WZOPPO
        if (KingdeeNameEnum.WZOPPO.name().equals(companyName) || KingdeeNameEnum.WZOPPO2016.name().equals(companyName)) {
            body = consignmentReportService.findConsignmentReport(dateStart, dateEnd);
        }

        return null;
    }
}
