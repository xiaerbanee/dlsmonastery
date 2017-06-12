package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BankInTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.service.BankService;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.service.BankInService;
import net.myspring.future.modules.crm.web.form.BankInAuditForm;
import net.myspring.future.modules.crm.web.form.BankInForm;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/bankIn")
public class BankInController {

    @Autowired
    private BankInService bankInService;
    @Autowired
    private BankService bankService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<BankInDto> list(Pageable pageable, BankInQuery bankInQuery){
        return bankInService.findPage(pageable, bankInQuery);
    }

    @RequestMapping(value = "getQuery")
    public BankInQuery getQuery(BankInQuery bankInQuery){

        List<String> processStatusList = new ArrayList<>();
        processStatusList.add("已通过");
        processStatusList.add("未通过");
        bankInQuery.getExtra().put("processStatusList",processStatusList);

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDateTimeUtils.getFirstDayOfMonth(today.atStartOfDay()).toLocalDate();
        bankInQuery.setCreatedDateRange(firstDayOfMonth.toString() + " - " + today.toString()  );

        return bankInQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(BankInForm bankInForm) {
        bankInService.save(bankInForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        bankInService.logicDelete(id);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }


    @RequestMapping(value = "audit")
    public RestResponse audit(BankInAuditForm bankInAuditForm) {

        bankInService.audit(bankInAuditForm);
        return new RestResponse("审核成功",ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(@RequestParam(value = "ids[]") String[] ids, boolean pass){

        bankInService.batchAudit(ids, pass);
        return new RestResponse("批量审核成功",ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "getForm")
    public BankInForm getForm(BankInForm bankInForm ){
        bankInForm.getExtra().put("typeList",BankInTypeEnum.getList());
        return bankInForm;
    }


    @RequestMapping(value = "findDto")
    public BankInDto findDto(String id ){
        if(StringUtils.isBlank(id)){
            return new BankInDto();
        }
        return bankInService.findDto(id);
    }


    @RequestMapping(value="export")
    public String export(BankInQuery bankInQuery) {

        return bankInService.export(bankInQuery);
    }


}
