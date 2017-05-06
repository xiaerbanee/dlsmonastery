package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.service.BankInService;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/bankIn")
public class BankInController {

    @Autowired
    private BankInService bankInService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<BankInDto> list(Pageable pageable, BankInQuery bankInQuery){
        Page<BankInDto> page = bankInService.findPage(pageable, bankInQuery);
        return page;
    }

    @RequestMapping(value = "getFormProperty")
    public BankInQuery getFormProperty(BankInQuery bankInQuery){
        bankInQuery.setProcessStatusList(new ArrayList<>());

        return bankInQuery;
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

    @RequestMapping(value = "findForm")
    public String findOne( ){
        return null;
    }

    private List<String> getActionList( ) {

        return null;
    }


}
