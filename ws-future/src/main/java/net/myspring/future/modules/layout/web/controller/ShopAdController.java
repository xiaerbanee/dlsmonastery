package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.service.ShopAdService;
import net.myspring.future.modules.layout.web.form.ShopAdAuditForm;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import net.myspring.future.modules.layout.web.validator.ShopAdValidator;
import net.myspring.util.excel.ExcelView;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "layout/shopAd")
public class ShopAdController {

    @Autowired
    private ShopAdService shopAdService;
    @Autowired
    private ShopAdValidator shopAdValidator;


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopAd:view')")
    public Page<ShopAdDto> list(Pageable pageable, ShopAdQuery shopAdQuery) {
        Page<ShopAdDto> page = shopAdService.findPage(pageable, shopAdQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:shopAd:view')")
    public ShopAdQuery getQuery(ShopAdQuery shopAdQuery) {
        return shopAdService.getQuery(shopAdQuery);
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:shopAd:edit')")
    public RestResponse save(ShopAdForm shopAdForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        shopAdValidator.validate(shopAdForm,bindingResult);
        if(bindingResult.hasErrors()){
            return  new RestResponse(bindingResult,"保存失败", ResponseCodeEnum.saved.name());
        }
        shopAdService.save(shopAdForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:shopAd:delete')")
    public RestResponse delete(String id) {
        shopAdService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:shopAd:view')")
    public ShopAdForm getForm(ShopAdForm shopAdForm) {
        return shopAdService.getForm(shopAdForm);
    }

    @RequestMapping(value = "getAuditForm")
    @PreAuthorize("hasPermission(null,'crm:shopAd:view')")
    public ShopAdAuditForm getAuditForm(ShopAdAuditForm shopAdAuditForm){
        return shopAdAuditForm;
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:shopAd:view')")
    public ShopAdDto detail(String id){
        return shopAdService.findOne(id);
    }

    @RequestMapping(value = "audit")
    @PreAuthorize("hasPermission(null,'crm:shopAd:edit')")
    public RestResponse audit(ShopAdAuditForm shopAdAuditForm) {
        String message = shopAdService.audit(shopAdAuditForm);
        if(message !=null){
            return new RestResponse("审批失败,原因："+message, ResponseCodeEnum.audited.name(),false);
        }else{
            return new RestResponse("审批成功", ResponseCodeEnum.audited.name());
        }

    }

    @RequestMapping(value = "batchAudit")
    @PreAuthorize("hasPermission(null,'crm:shopAd:edit')")
    public RestResponse batchAudit(@RequestParam(value = "ids[]")String[] ids, Boolean pass) {
        String message = shopAdService.batchAudit(ids,pass);
        if(message !=null){
            return new RestResponse("批量审批部分失败,原因："+message, ResponseCodeEnum.audited.name(),false);
        }else{
            return new RestResponse("批量审批成功", ResponseCodeEnum.audited.name());
        }
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopAd:view')")
    public ModelAndView export(ShopAdQuery shopAdQuery) throws IOException{
        return new ModelAndView(new ExcelView(),"simpleExcelBook",shopAdService.export(shopAdQuery));
    }
}
