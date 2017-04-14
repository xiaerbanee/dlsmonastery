package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productImeUpload")
public class ProductImeUploadController {


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
    public String save(ProductImeUpload productImeUpload, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(ProductImeUpload productImeUpload, BindingResult bindingResult){
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        return null;
    }

    private List<String> getActionList(ProductImeUpload productImeUpload) {
        return null;
    }

}
