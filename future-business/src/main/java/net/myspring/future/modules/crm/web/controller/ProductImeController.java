package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/4/2.
 */

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {
    @Autowired
    private ProductImeService productImeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        Pageable pageable = new PageRequest(0,100);
        Page<ProductIme> page = productImeService.findPage(pageable);
        return ObjectMapperUtils.writeValueAsString(page);
    }
}
