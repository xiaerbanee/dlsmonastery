package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ExpressCompanyService;
import net.myspring.future.modules.crm.service.ExpressOrderService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/expressOrder")
public class ExpressOrderController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }
    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
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
