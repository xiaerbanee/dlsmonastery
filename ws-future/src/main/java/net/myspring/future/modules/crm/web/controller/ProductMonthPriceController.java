package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
