package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.SalProxyReceiveDto;
import net.myspring.cloud.modules.report.service.SalProxyReceiveService;
import net.myspring.cloud.modules.report.web.query.SalProxyReceiveQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 代理销售收款汇总报表
 */
@RestController
@RequestMapping(value = "report/salProxyReceive")
public class SalProxyReceiveController {
    @Autowired
    private SalProxyReceiveService salProxyReceiveService;

    @RequestMapping(value = "list")
    public List<SalProxyReceiveDto> list (SalProxyReceiveQuery salProxyReceiveQuery){
        return salProxyReceiveService.getSalProxyReceiveReport(salProxyReceiveQuery);
    }

    @RequestMapping(value = "export")
    public ModelAndView export (String dateStart, String dateEnd ){
        if (dateStart != null && dateEnd != null) {
            return new ModelAndView(new ExcelView(), "simpleExcelBook", salProxyReceiveService.export(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd)));
        }
        return null;
    }
}
