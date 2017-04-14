package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ExpressCompany;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/expressCompany")
public class ExpressCompanyController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(ExpressCompany expressCompany, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(ExpressCompany expressCompany) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(ExpressCompany expressCompany){
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

    private List<String> getActionList() {
        return null;
    }
}
