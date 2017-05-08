package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.input.service.BdCustomerService;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.report.dto.ReceivableForDetailDto;
import net.myspring.cloud.modules.report.dto.ReceivableForSummaryDto;
import net.myspring.cloud.modules.report.service.ReceivableReportService;
import net.myspring.cloud.modules.report.web.form.ReceivableReportForm;
import net.myspring.cloud.modules.report.web.query.ReceivableReportQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2016/12/20.
 */
@RestController
@RequestMapping(value="report/receivableReport")
public class ReceivableReportController {
    @Autowired
    private ReceivableReportService receivableReportService;
    @Autowired
    private BdCustomerService bdCustomerService;

    @RequestMapping(value="summaryList")
    public ReceivableReportForm summaryList(ReceivableReportQuery receivableReportQuery) {
        ReceivableReportForm form = new ReceivableReportForm();
        LocalDate dateStart = receivableReportQuery.getStartDate();
        LocalDate dateEnd = receivableReportQuery.getEndDate();
        String primaryGroupId;
        List<NameNumberDto> groupList = bdCustomerService.findPrimaryGroupAndPrimaryGroupName();
        if (StringUtils.isBlank(receivableReportQuery.getPrimaryGroupId())) {
            primaryGroupId = groupList.get(1).getNumber();
            form.setPrimaryGroupName(groupList.get(1).getName());
        }else {
            primaryGroupId = receivableReportQuery.getPrimaryGroupId();
        }
        List<ReceivableForSummaryDto> receivableReportForSummaryList = receivableReportService.getSummaryList(dateStart, dateEnd,primaryGroupId);
        form.setReceivableSummaryList(receivableReportForSummaryList);
        form.setDateRange(dateStart + CharConstant.DATE_RANGE_SPLITTER + dateEnd);
        form.setPrimaryGroup(groupList);
        return form;
    }

    @RequestMapping(value = "detailList")
    public List<ReceivableForDetailDto> detailList(ReceivableReportQuery receivableReportQuery){
        LocalDate dateStart = receivableReportQuery.getStartDate();
        LocalDate dateEnd = receivableReportQuery.getEndDate();
        String customerId = receivableReportQuery.getCustomerId();
        return receivableReportService.getDetailList(dateStart, dateEnd,customerId);
    }
}
