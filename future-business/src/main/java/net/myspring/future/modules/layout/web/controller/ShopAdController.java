package net.myspring.future.modules.layout.web.controller;


import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopAd")
public class ShopAdController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty( ) {
        return null;
    }
    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public String getQuery( ) {
        return null;
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
