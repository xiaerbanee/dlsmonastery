package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.crm.domain.Express;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/express")
public class ExpressController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(Depot depot) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(Express express){
        return null;

    }

    @RequestMapping(value = "delete")
    public String delete(Express express, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(Express express){
        return null;
    }

    private List<String> getActionList() {
        return null;
    }


}
