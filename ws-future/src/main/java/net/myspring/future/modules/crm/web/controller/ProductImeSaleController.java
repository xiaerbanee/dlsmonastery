package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductImeSale;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productImeSale")
public class ProductImeSaleController {


    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "getForm")
    public String getForm(ProductImeSale productImeSale) {
        return null;
    }
    @RequestMapping(value = "save")
    public String save(ProductImeSale productImeSale, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(ProductImeSale productImeSale, BindingResult bindingResult){
        return null;
    }
    private List<String> getActionList(ProductImeSale productImeSale) {
        return null;
    }
}
