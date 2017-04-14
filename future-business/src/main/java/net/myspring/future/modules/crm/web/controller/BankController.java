package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.Bank;
import net.myspring.future.modules.crm.service.BankService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/bank")
public class BankController {

    @Autowired
    private BankService bankService;



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){

        return null;
    }

    @RequestMapping(value = "save")
    public String save(Bank bank){
        bankService.save(bank);
        return null;

    }

    @RequestMapping(value = "syn")
    public String syn(){
        bankService.syn();
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(Bank bank, BindingResult bindingResult) {
        bankService.delete(bank);

        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(Bank bank){
        return null;
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();

        return actionList;
    }

}
