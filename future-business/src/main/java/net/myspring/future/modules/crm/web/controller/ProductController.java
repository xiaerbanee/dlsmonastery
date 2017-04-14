package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.Product;
import net.myspring.future.modules.crm.service.ProductService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/product")
public class ProductController {

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "filter")
    public String filter(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(Product product){
        return null;
    }

    @RequestMapping(value = "findHasImeProduct")
    public String findHasImeProduct(){
        return null;
    }

    @RequestMapping(value = "searchAll")
    public String searchAll(String name,String code){
        List<Product> productList = Lists.newArrayList();
        return null;
    }

    @RequestMapping(value = "search")
    public String search(String name,String code){
        return null;
    }

    @RequestMapping(value = "save")
    public String save(Product product) {
        return null;
    }

    @RequestMapping(value = "syn")
    public String syn() {
        return null;
    }

    private List<String> getActionList(Product product) {
        return null;
    }
}
