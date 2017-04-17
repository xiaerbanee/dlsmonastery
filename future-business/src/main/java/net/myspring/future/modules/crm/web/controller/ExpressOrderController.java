package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/expressOrder")
public class ExpressOrderController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }
    @RequestMapping(value = "getQuery")
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(ProductImeSale productImeSale) {
        return null;
    }

    @RequestMapping(value = "update")
    public String save(ExpressOrder expressOrder){
        return null;
    }

    @RequestMapping(value = "resetPrintStatus")
    public String delete(ExpressOrder expressOrder, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(ExpressOrder expressOrder){
        return null;
    }

    private List<String> getActionList() {
        return null;
    }

}
