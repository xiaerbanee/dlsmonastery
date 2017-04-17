package net.myspring.future.modules.basic.web.controller;


import net.myspring.future.modules.basic.domain.ProductType;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productType")
public class ProductTypeController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        return null;
    }
    @RequestMapping(value = "save")
    public String save(ProductType productType) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(ProductType productType){
        return null;
    }
    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        return null;
    }

    @RequestMapping(value="getQuery")
    public String getQuery(){
        return null;
    }

    @RequestMapping(value = "search")
    public String search(String name){
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        return null;
    }
    private List<String> getActionList() {
        return null;
    }
}
