package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.service.ProductMonthPriceService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productMonthPrice")
public class ProductMonthPriceController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getQuery")
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }
    @RequestMapping(value = "getFormProperty")
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
