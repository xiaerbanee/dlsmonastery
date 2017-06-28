package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutySignDto;
import net.myspring.basic.modules.hr.service.DutySignService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.DutySignForm;
import net.myspring.basic.modules.hr.web.query.DutySignQuery;
import net.myspring.basic.modules.hr.web.validator.DutySignValidator;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by liuj on 2016/11/30.
 */
@RestController
@RequestMapping(value = "hr/dutySign")
public class DutySignController {

    @Autowired
    private DutySignService dutySignService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DutySignValidator dutySignValidator;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutySignDto> list(Pageable pageable, DutySignQuery dutySignQuery) {
        Page<DutySignDto> page = dutySignService.findPage(pageable,dutySignQuery);
        for(DutySignDto dutySignDto:page.getContent()){
            setOperationStatus(dutySignDto);
        }
        return page;
    }

    @RequestMapping(value = "getQuery")
    public DutySignQuery getQuery(DutySignQuery dutySignQuery) {
        dutySignQuery.getExtra().put("officeList", officeService.findAll());
        dutySignQuery.getExtra().put("positionList", positionService.findAll());
        return dutySignQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutySignForm dutySignForm, BindingResult bindingResult) {
        dutySignValidator.validate(dutySignForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        dutySignService.save(dutySignForm);
        return new RestResponse("保存成功", null);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutySignService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "getForm")
    public DutySignForm detail(DutySignForm dutySignForm) {
        dutySignForm = dutySignService.getForm(dutySignForm);
        return dutySignForm;
    }

    @RequestMapping(value = "export",method = RequestMethod.GET)
    public ModelAndView export(DutySignQuery dutySignQuery) throws IOException {
        SimpleExcelBook simpleExcelBook = dutySignService.findSimpleExcelSheet(dutySignQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

    private void setOperationStatus(DutySignDto dutySign) {
        if(dutySign.getCreatedBy().equals(RequestUtils.getAccountId()) && AuditTypeEnum.APPLY.getValue().equals(dutySign.getStatus())) {
            dutySign.setDeleted(true);
        }
    }
}
