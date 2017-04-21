package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.service.BankService;
import net.myspring.future.modules.basic.web.Query.BankQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "basic/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<BankDto> list(Pageable pageable, BankQuery bankQuery){
        Page<BankDto> page = bankService.findPage(pageable, bankQuery);
        return page;
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
