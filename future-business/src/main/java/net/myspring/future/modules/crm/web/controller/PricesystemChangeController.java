package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.PricesystemChange;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/pricesystemChange")
public class PricesystemChangeController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value="getListProperty")
    public String form(){
        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        return null;
    }

    @RequestMapping(value = "save")
    public String save(PricesystemChange pricesystemChange, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        return null;
    }

    @RequestMapping(value = "getPricesystemDetail")
    public String getPricesystemDetail(@RequestParam(value = "productIdList[]") String[] proudctIdList){
        return null;
    }

    private List<String> getActionList(PricesystemChange pricesystemChange) {
        return null;
    }
}
