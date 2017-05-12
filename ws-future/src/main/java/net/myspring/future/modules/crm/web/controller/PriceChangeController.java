package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.service.PriceChangeService;
import net.myspring.future.modules.crm.web.query.PriceChangeQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/priceChange")
public class PriceChangeController {

    @Autowired
    private PriceChangeService priceChangeService;
    @Autowired
    private ProductTypeService productTypeService;


    @ModelAttribute
    public PriceChange get(@RequestParam(required = false) String id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(PriceChange priceChange, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(PriceChange priceChange) {
        return null;
    }


    @RequestMapping(value = "check")
    public String check(PriceChange priceChange) {
        return null;
    }

    @RequestMapping(value = "findForm")
    public String findOne(PriceChange priceChange){
        return null;
    }

    @RequestMapping(value="getQuery")
    public PriceChangeQuery getQuery(PriceChangeQuery priceChangeQuery){
        return priceChangeQuery;
    }

    private List<String> getActionList(PriceChange priceChange) {
        return null;
    }

}
