package net.myspring.future.modules.crm.web.controller;


import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/bankIn")
public class BankInController {





    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){

        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(){


        return null;
    }

    @RequestMapping(value = "getQuery")
    public String getQuery(){


        return null;
    }

    @RequestMapping(value = "save")
    public String save( ){

        return null;
    }

    @RequestMapping(value = "delete")
    public String delete( ) {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit( ){


        return null;
    }

    @RequestMapping(value = "batchAudit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){

        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne( ){
        return null;
    }

    private List<String> getActionList( ) {

        return null;
    }


}
