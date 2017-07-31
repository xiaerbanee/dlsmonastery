package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.crm.service.ProductMonthPriceService;
import net.myspring.future.modules.crm.web.query.ProductMonthPriceSumQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productMonthPriceSum")
public class ProductMonthPriceSumController {

    @Autowired
    private OfficeClient officeClient;

    @Autowired
    private ProductMonthPriceService productMonthPriceService;

    @RequestMapping
    @PreAuthorize("hasPermission(null,'crm:productMonthPrice:sum')")
    public Map<String,Object> list(ProductMonthPriceSumQuery productMonthPriceSumQuery) {

        Map<String, Object> map = Maps.newHashMap();
        List<List<Object>> datas = Lists.newArrayList();
        List<Object> header = productMonthPriceService.getHeaders(productMonthPriceSumQuery.getMonth());
        map.put("header", header);
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.getAreaId())) {
            datas = productMonthPriceService.getDatas(productMonthPriceSumQuery, false);
        }
        map.put("datas", datas);
        return map;
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public ProductMonthPriceSumQuery getQuery(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        productMonthPriceSumQuery.getExtra().put("areaList",officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        productMonthPriceSumQuery.getExtra().put("statusList", AuditStatusEnum.getList().subList(0, 2));
        return productMonthPriceSumQuery;
    }

    @RequestMapping(value = "export")
    @PreAuthorize("hasPermission(null,'crm:productMonthPrice:sum')")
    public ModelAndView export(ProductMonthPriceSumQuery productMonthPriceSumQuery) {

        if (StringUtils.isBlank(productMonthPriceSumQuery.getMonth())) {
            productMonthPriceSumQuery.setMonth(LocalDateUtils.format(LocalDate.now().minusMonths(1),"yyyy-MM"));
        }else{
            productMonthPriceSumQuery.setMonth(productMonthPriceSumQuery.getMonth().substring(0, 7));
        }
        return new ModelAndView(new ExcelView(), "simpleExcelBook", productMonthPriceService.export(productMonthPriceSumQuery));
    }

    @RequestMapping(value = "uploadAudit")
    public RestResponse uploadAudit(ProductMonthPriceSumQuery productMonthPriceSumQuery) {

        productMonthPriceService.uploadAudit(productMonthPriceSumQuery);
        return new RestResponse("审批成功", ResponseCodeEnum.audited.name());

    }


}
