package net.myspring.future.modules.layout.web.controller;


import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.service.ShopAdService;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/shopAd")
public class ShopAdController {

    @Autowired
    private ShopAdService shopAdService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopAdDto> list(Pageable pageable, ShopAdQuery shopAdQuery) {
        return shopAdService.findPage(pageable,shopAdQuery);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty( ) {
        return null;
    }

    @RequestMapping(value = "getQuery")
    public ShopAdQuery getQuery(ShopAdQuery shopAdQuery) {
        return shopAdQuery;
    }

    @RequestMapping(value = "save")
    public String save( ) {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete( ) {
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail( ) {
        return null;
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit( ) {
        return null;
    }

    @RequestMapping(value = "batchAudit")
    public String batchAudit(Boolean pass, @RequestParam(value = "ids[]")String[] ids) {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(org.apache.catalina.servlet4preview.http.HttpServletRequest request) {
        return null;
    }

    private List<String> getActionList( ) {
        return null;
    }
}
