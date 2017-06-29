package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BankInTypeEnum;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.service.BankInService;
import net.myspring.future.modules.crm.web.form.BankInAuditForm;
import net.myspring.future.modules.crm.web.form.BankInForm;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "crm/bankIn")
public class BankInController {

    @Autowired
    private BankInService bankInService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:bankIn:view')")
    public Page<BankInDto> list(Pageable pageable, BankInQuery bankInQuery){
        return bankInService.findPage(pageable, bankInQuery);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:bankIn:view')")
    public BankInQuery getQuery(BankInQuery bankInQuery){
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDateTimeUtils.getFirstDayOfMonth(today.atStartOfDay()).toLocalDate();
        bankInQuery.setCreatedDateRange(firstDayOfMonth.toString() + " - " + today.toString()  );
        return bankInQuery;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:bankIn:edit')")
    public RestResponse save(BankInForm bankInForm) {
        if(bankInForm.getAmount() == null || bankInForm.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("到账金额必须大于0");
        }
        bankInService.save(bankInForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:bankIn:delte')")
    public RestResponse delete(String id) {
        bankInService.logicDelete(id);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "audit")
    @PreAuthorize("hasPermission(null,'crm:bankIn:audit')")
    public RestResponse audit(BankInAuditForm bankInAuditForm) {

        if(bankInAuditForm.getPass()== null || bankInAuditForm.getSyn() == null || bankInAuditForm.getBillDate() == null){
            throw new ServiceException("审批结果、是否同步财务、开单日期均不能为空");
        }

        bankInService.audit(bankInAuditForm);
        return new RestResponse("审核成功",ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "batchAudit")
    @PreAuthorize("hasPermission(null,'crm:bankIn:audit')")
    public RestResponse batchAudit(@RequestParam(value = "ids[]") String[] ids, boolean pass){

        if(ids == null || ids.length == 0){
            throw new ServiceException("请选择需要批量审批的记录");
        }
        LocalDate billDate = LocalDate.now();

        List<String> idList = Arrays.asList(ids);
        for(String id : idList){
            BankInAuditForm bankInAuditForm = new BankInAuditForm();
            bankInAuditForm.setAuditRemarks("批量通过");
            bankInAuditForm.setId(id);
            bankInAuditForm.setPass(pass);
            bankInAuditForm.setSyn(true);
            bankInAuditForm.setBillDate(billDate);

            bankInService.audit(bankInAuditForm);
        }

        return new RestResponse("批量审核成功",ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:bankIn:view')")
    public BankInForm getForm(BankInForm bankInForm ){
        bankInForm.getExtra().put("typeList",BankInTypeEnum.getList());
        return bankInForm;
    }

    @RequestMapping(value = "getAuditForm")
    @PreAuthorize("hasPermission(null,'crm:bankIn:view')")
    public BankInAuditForm getAuditForm(BankInAuditForm bankInAuditForm ){
        bankInAuditForm.getExtra().put("defaultBillDate", LocalDate.now());
        return bankInAuditForm;
    }

    @RequestMapping(value = "findDto")
    @PreAuthorize("hasPermission(null,'crm:bankIn:view')")
    public BankInDto findDto(String id ){
        if(StringUtils.isBlank(id)){
            return new BankInDto();
        }
        return bankInService.findDto(id);
    }

    @RequestMapping(value="export")
    @PreAuthorize("hasPermission(null,'crm:bankIn:view')")
    public ModelAndView export(BankInQuery bankInQuery) {
        return new ModelAndView(new ExcelView(), "simpleExcelBook", bankInService.export(bankInQuery));
    }


}
