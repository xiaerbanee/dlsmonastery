package net.myspring.report.modules.future.web.controller;

import net.myspring.report.common.utils.RequestUtils;
import net.myspring.report.modules.future.client.OfficeClient;
import net.myspring.report.modules.future.dto.DepotDto;
import net.myspring.report.modules.future.service.DepotService;
import net.myspring.report.modules.future.service.ProductMonthPriceSumService;
import net.myspring.report.modules.future.web.query.ProductMonthPriceSumQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "crm/productMonthPriceSum")
public class ProductMonthPriceSumController {

    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ProductMonthPriceSumService productMonthPriceService;

    @RequestMapping(value = "export")
    @PreAuthorize("hasPermission(null,'crm:productMonthPrice:sum')")
    public ModelAndView export(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        if (StringUtils.isBlank(productMonthPriceSumQuery.getMonth())) {
            productMonthPriceSumQuery.setMonth(LocalDateUtils.format(LocalDate.now().minusMonths(1),"yyyy-MM"));
        }
        String accountId= RequestUtils.getAccountId();
        productMonthPriceSumQuery.setDepotIdList(depotService.findByAccountId(accountId).stream().map(DepotDto::getId).collect(Collectors.toList()));
        return new ModelAndView(new ExcelView(), "simpleExcelBook", productMonthPriceService.export(productMonthPriceSumQuery));
    }
}
