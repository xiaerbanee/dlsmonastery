package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.dto.ShopBuildDto;
import net.myspring.future.modules.layout.service.ShopBuildService;
import net.myspring.future.modules.layout.web.form.ShopBuildDetailOrAuditForm;
import net.myspring.future.modules.layout.web.form.ShopBuildForm;
import net.myspring.future.modules.layout.web.query.ShopBuildQuery;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/shopBuild")
public class ShopBuildController {

    @Autowired
    private ShopBuildService shopBuildService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopBuildDto> list(Pageable pageable, ShopBuildQuery shopBuildQuery) {
        Page<ShopBuildDto> page = shopBuildService.findPage(pageable,shopBuildQuery);
        return page;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getQuery")
    public ShopBuildQuery getQuery(ShopBuildQuery shopBuildQuery) {
        return shopBuildQuery;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(ShopBuildForm shopBuildForm) {
        shopBuildService.save(shopBuildForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm){
        shopBuildService.audit(shopBuildDetailOrAuditForm);
        RestResponse restResponse = new RestResponse("审核成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(@RequestParam(value = "ids[]") String[] ids, String pass) {
        shopBuildService.batchAudit(ids,pass);
        RestResponse restResponse = new RestResponse("批量审批成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }


    @RequestMapping(value = "delete")
    public RestResponse delete(ShopBuildForm shopBuildForm) {
        shopBuildService.logicDeleteOne(shopBuildForm.getId());
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }
    @RequestMapping(value = "getFormProperty")
    public ShopBuildForm getFormProperty(ShopBuildForm shopBuildForm) {
        return shopBuildService.getFormProperty(shopBuildForm);
    }

    @RequestMapping(value = "detail")
    public ShopBuildDetailOrAuditForm detail(ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm){
        ShopBuildDto shopBuildDto = shopBuildService.detail(shopBuildDetailOrAuditForm);
        shopBuildDetailOrAuditForm.setShopBuildDto(shopBuildDto);
        return shopBuildDetailOrAuditForm;
    }


    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(ShopBuildQuery shopBuildQuery) throws IOException{
        Workbook workbook = new SXSSFWorkbook(10000);
        return shopBuildService.findSimpleExcelSheets(workbook,shopBuildQuery);
    }

    private List<String> getActionList() {
        return null;
    }
}
