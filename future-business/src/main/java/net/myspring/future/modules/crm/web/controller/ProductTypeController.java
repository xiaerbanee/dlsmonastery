package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ProductType;
import net.myspring.future.modules.crm.service.ProductTypeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
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
