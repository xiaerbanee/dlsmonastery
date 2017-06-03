package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.service.ShopAdService;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public RestResponse save(ShopAdForm shopAdForm) {
        shopAdService.save(shopAdForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
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

    @RequestMapping(value = "findOne")
    public ShopAdDto detail(String id){
        return shopAdService.findOne(id);
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(ShopAdForm shopAdForm) {
        String message = shopAdService.audit(shopAdForm);
        if(message !=null){
            return new RestResponse("审批失败,原因："+message, ResponseCodeEnum.audited.name(),false);
        }else{
            return new RestResponse("审批成功"+message, ResponseCodeEnum.audited.name());
        }

    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(@RequestParam(value = "ids[]")String[] ids, Boolean pass) {
        String message = shopAdService.batchAudit(ids,pass);
        if(message !=null){
            return new RestResponse("批量审批部分失败,原因："+message, ResponseCodeEnum.audited.name(),false);
        }else{
            return new RestResponse("批量审批成功"+message, ResponseCodeEnum.audited.name());
        }
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(ShopAdQuery shopAdQuery) throws IOException{
        Workbook workbook = new SXSSFWorkbook(10000);
        return shopAdService.findSimpleExcelSheets(workbook,shopAdQuery);
    }

}
