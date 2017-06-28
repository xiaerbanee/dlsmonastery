package net.myspring.cloud.modules.report.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.report.service.RetailAccountService;
import net.myspring.cloud.modules.report.web.query.RetailAccountQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 零售费用报表
 * Created by lihx on 2017/6/28.
 */
@RestController
@RequestMapping(value = "report/retailAccount")
public class RetailAccountController {
    @Autowired
    private RetailAccountService retailAccountService;


    @RequestMapping(value = "list")
    public  List<List<Object>> list(RetailAccountQuery retailAccountQuery) {
        List<List<Object>> retailShopReport = Lists.newArrayList();
//            nestedHeader = retailReportToolsService.getNestedHeaders(start, end);

        return  retailAccountService.getRetailReport(retailAccountQuery);
    }
}
