package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.service.ShopAdService;
import net.myspring.future.modules.layout.web.form.ShopAdAuditForm;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "layout/shopAd")
public class ShopAdController {

    @Autowired
    private ShopAdService shopAdService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopAdDto> list(Pageable pageable, ShopAdQuery shopAdQuery) {
        return shopAdService.findPage(pageable,shopAdQuery);
    }

    @RequestMapping(value = "getQuery")
    public ShopAdQuery getQuery(ShopAdQuery shopAdQuery) {
        return shopAdService.getQuery(shopAdQuery);
    }

    @RequestMapping(value = "save")
    public RestResponse save(@Valid ShopAdForm shopAdForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("签到成功", null);
        if(bindingResult.hasErrors()){
            return  new RestResponse(bindingResult,"签到失败", null);
        }
        shopAdService.save(shopAdForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        shopAdService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    public ShopAdForm getForm(ShopAdForm shopAdForm) {
        return shopAdService.getForm(shopAdForm);
    }

    @RequestMapping(value = "getAuditForm")
    public ShopAdAuditForm getAuditForm(ShopAdAuditForm shopAdAuditForm){
        return shopAdAuditForm;
    }

    @RequestMapping(value = "findOne")
    public ShopAdDto detail(String id){
        return shopAdService.findOne(id);
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(ShopAdAuditForm shopAdAuditForm) {
        String message = shopAdService.audit(shopAdAuditForm);
        if(message !=null){
            return new RestResponse("审批失败,原因："+message, ResponseCodeEnum.audited.name(),false);
        }else{
            return new RestResponse("审批成功", ResponseCodeEnum.audited.name());
        }

    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(@RequestParam(value = "ids[]")String[] ids, Boolean pass) {
        String message = shopAdService.batchAudit(ids,pass);
        if(message !=null){
            return new RestResponse("批量审批部分失败,原因："+message, ResponseCodeEnum.audited.name(),false);
        }else{
            return new RestResponse("批量审批成功", ResponseCodeEnum.audited.name());
        }
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(ShopAdQuery shopAdQuery) throws IOException{
        Workbook workbook = new SXSSFWorkbook(10000);
        return shopAdService.findSimpleExcelSheets(workbook,shopAdQuery);
    }

}
