package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.NestedHeaderCellDto;
import net.myspring.cloud.modules.report.manager.RetailAccountManager;
import net.myspring.cloud.modules.report.service.RetailAccountService;
import net.myspring.cloud.modules.report.web.query.RetailAccountQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
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
    @Autowired
    private RetailAccountManager retailAccountManager;


    @RequestMapping(value = "list")
    public  List<List<Object>> list(RetailAccountQuery retailAccountQuery) {
        YearMonth start = retailAccountQuery.getMonthStart();
        YearMonth end = retailAccountQuery.getMonthEnd();
        List<List<Object>> retailShopReport = retailAccountService.getRetailReport(retailAccountQuery);
        List<List<NestedHeaderCellDto>>  nestedHeader = retailAccountManager.getNestedHeaders(start, end);

        return  null;
    }
}
