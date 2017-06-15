package net.myspring.future.modules.crm.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.DepotChangeEnum;
import net.myspring.future.modules.crm.dto.DepotChangeDto;
import net.myspring.future.modules.crm.service.DepotChangeService;
import net.myspring.future.modules.crm.web.form.DepotChangeAuditForm;
import net.myspring.future.modules.crm.web.form.DepotChangeForm;
import net.myspring.future.modules.crm.web.query.DepotChangeQuery;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "crm/depotChange")
public class DepotChangeController {

    @Autowired
    private DepotChangeService depotChangeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotChangeDto> list(Pageable pageable, DepotChangeQuery depotChangeQuery){
        return depotChangeService.findPage(pageable,depotChangeQuery);
    }

    @RequestMapping(value = "save")
    public RestResponse save(DepotChangeForm depotChangeForm) {
        depotChangeService.save(depotChangeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        depotChangeService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "getForm")
    public DepotChangeForm getForm(DepotChangeForm depotChangeForm) {
        return depotChangeService.getForm(depotChangeForm);
    }

    @RequestMapping(value = "getAuditForm")
    public DepotChangeAuditForm getAuditForm(DepotChangeAuditForm depotChangeAuditForm){
        return depotChangeAuditForm;
    }

    @RequestMapping(value = "getQuery")
    public DepotChangeQuery getQuery(DepotChangeQuery depotChangeQuery) {
        depotChangeQuery.getExtra().put("types", DepotChangeEnum.getList());
        return depotChangeQuery;
    }

    @RequestMapping(value = "findOne")
    public DepotChangeDto findOne(String id){
        return depotChangeService.findOne(id);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail( ) {
        return null;
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(DepotChangeAuditForm depotChangeAuditForm){
        depotChangeService.audit(depotChangeAuditForm);
        return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(DepotChangeQuery depotChangeQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        return depotChangeService.findSimpleExcelSheets(workbook,depotChangeQuery);
    }

}
