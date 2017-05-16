package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductImeDto> list(Pageable pageable, ProductImeQuery productImeQuery){
        return productImeService.findPage(pageable,productImeQuery);

    }

    @RequestMapping(value = "getQuery")
    public ProductImeQuery getQuery(ProductImeQuery productImeQuery) {
         productImeQuery.setInputTypes(productImeService.findInputTypes());
         return productImeQuery;

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
