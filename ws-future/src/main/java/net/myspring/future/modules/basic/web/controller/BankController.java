package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.service.BankService;
import net.myspring.future.modules.basic.web.Query.BankQuery;
import net.myspring.future.modules.basic.web.form.BankForm;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "basic/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<BankDto> list(Pageable pageable,BankQuery bankQuery){
        Page<BankDto> page = bankService.findPage(pageable,bankQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(BankForm bankForm){
        bankService.save(bankForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "syn")
    public String syn(){
        bankService.syn();
        return ObjectMapperUtils.writeValueAsString(new RestResponse("同步成功",null));
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Bank bank,BindingResult bindingResult) {
        bankService.delete(bank);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findOne")
    public BankForm findOne(BankForm bankForm){
        bankForm=bankService.findForm(bankForm);
        return bankForm;
    }
}