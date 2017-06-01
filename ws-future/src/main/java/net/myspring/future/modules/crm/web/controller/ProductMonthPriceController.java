package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDto;
import net.myspring.future.modules.crm.service.ProductMonthPriceService;
import net.myspring.future.modules.crm.web.query.ProductMonthPriceQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productMonthPrice")
public class ProductMonthPriceController {

    @Autowired
    private ProductMonthPriceService productMonthPriceService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductMonthPriceDto> list(Pageable pageable, ProductMonthPriceQuery ProductMonthPriceQuery){
        Page<ProductMonthPriceDto> page = productMonthPriceService.findPage(pageable,ProductMonthPriceQuery);
        return page;
    }
    @RequestMapping(value = "getQuery")
    public ProductMonthPriceQuery getQuery(ProductMonthPriceQuery productMonthPriceQuery) {
        return productMonthPriceQuery;
    }

    @RequestMapping(value = "getForm")
    public String getForm() {
        return null;
    }
    @RequestMapping(value = "findOne")
    public String findOne(ProductMonthPrice productMonthPrice){
        return null;
    }


    @RequestMapping(value = "checkMonth")
    public String checkMonth(String month) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(ProductMonthPrice productMonthPrice) {
        return null;
    }

    private List<String> getActionList(ProductMonthPrice productMonthPrice) {
        return null;
    }

}
