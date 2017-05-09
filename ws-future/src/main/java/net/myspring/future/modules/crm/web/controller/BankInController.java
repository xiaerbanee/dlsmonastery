package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BankInTypeEnum;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.service.BankService;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.service.BankInService;
import net.myspring.future.modules.crm.web.form.BankInForm;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "crm/bankIn")
public class BankInController {

    @Autowired
    private BankInService bankInService;

    @Autowired
    private BankService bankService;

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
    public RestResponse save(@Valid BankInForm bankInForm, BindingResult result) {

        if(result.hasErrors()){
            return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        }

        bankInService.save(bankInForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        bankInService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }


    @RequestMapping(value = "audit")
    public String audit( ){


        return null;
    }


    @RequestMapping(value = "findDetail")
    public BankInDto findDetail(String id ){
        return bankInService.findDetail(id);
    }




    @RequestMapping(value = "batchAudit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){

        return null;
    }

    @RequestMapping(value = "findForm")
    public BankInForm findForm(BankInForm bankInForm ){
        BankInForm  result =bankInService.findForm(bankInForm);

        result.setTypeList(BankInTypeEnum.getList());
        result.setBankDtoList(bankService.findBankDtosByAccountId(SecurityUtils.getAccountId()));
        return result;
    }

}
