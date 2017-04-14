package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ExpressCompanyService;
import net.myspring.future.modules.crm.service.ExpressService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
