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
    public RestResponse delete(ShopAdForm shopAdForm) {
        shopAdService.logicDelete(shopAdForm.getId());
        return new RestResponse("删除成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findForm")
    public ShopAdForm findForm(ShopAdForm shopAdForm) {
        return shopAdService.findForm(shopAdForm);
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(ShopAdForm shopAdForm) {
        shopAdService.audit(shopAdForm);
        return new RestResponse("审批成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(ShopAdForm shopAdForm) {
        shopAdService.batchAudit(shopAdForm);
        return new RestResponse("审批成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(ShopAdQuery shopAdQuery) throws IOException{
        Workbook workbook = new SXSSFWorkbook(10000);
        return shopAdService.findSimpleExcelSheets(workbook,shopAdQuery);
    }

    private List<String> getActionList( ) {
        return null;
    }
}
