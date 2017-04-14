package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductImeSale;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productImeSale")
public class ProductImeSaleController {


    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(ProductImeSale productImeSale) {
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
