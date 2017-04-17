package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.crm.service.ProductImeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(Depot depot) {
        return null;
    }


    @RequestMapping(value = "search")
    public String findByImeList(String imeStr ) {
        return null;
    }

    @RequestMapping(value = "searchIme")
    public String searchIme(String ime ) {
        return null;
    }

    @RequestMapping(value = "searchByStore")
    public String searchByStore(String depotId,String ime ) {
        return null;
    }

    @RequestMapping(value = "detail")
    public String detail(String productImeId){
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
