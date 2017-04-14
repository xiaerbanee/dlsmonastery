package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.domain.AdPricesystemChange;
import net.myspring.future.modules.crm.domain.Product;
import net.myspring.future.modules.crm.service.AdPricesystemChangeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/adPricesystemChange")
public class AdPricesystemChangeController {

    @Autowired
    private AdPricesystemChangeService adPricesystemChangeService;

    @ModelAttribute
    public AdPricesystemChange get(@RequestParam(required = false) String id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){

        return null;
    }

    @RequestMapping(value="findFilter", method = RequestMethod.GET)
    public String findFilter(HttpServletRequest request){

        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(String data){


        return null;
    }


}
