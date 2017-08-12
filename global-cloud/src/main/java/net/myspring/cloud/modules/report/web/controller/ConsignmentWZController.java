package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.ConsignmentDto;
import net.myspring.cloud.modules.report.service.ConsignmentWZService;
import net.myspring.cloud.modules.report.web.query.ConsignmentWZQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 委托代销（温州）
 * Created by lihx on 2017/8/12.
 */
@RestController
@RequestMapping(value = "report/consignmentWZ")
public class ConsignmentWZController {
    @Autowired
    private ConsignmentWZService consignmentWZService;

    @RequestMapping(value = "list")
    public List<ConsignmentDto> list (ConsignmentWZQuery consignmentWZQuery){
        return consignmentWZService.findConsignmentReport(consignmentWZQuery);
    }

    @RequestMapping(value = "export")
    public ModelAndView export (String dateStart, String dateEnd ){
        if (dateStart != null && dateEnd != null) {
            return new ModelAndView(new ExcelView(), "simpleExcelBook", consignmentWZService.export(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd)));
        }
        return null;
    }
}
