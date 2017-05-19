package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.dto.DutySignDto;
import net.myspring.basic.modules.hr.service.DutySignService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.DutySignForm;
import net.myspring.basic.modules.hr.web.query.DutySignQuery;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelSheet;
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

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutySignDto> list(Pageable pageable, DutySignQuery dutySignQuery) {
        Page<DutySignDto> page = dutySignService.findPage(pageable,dutySignQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public DutySignQuery getQuery(DutySignQuery dutySignQuery) {
        dutySignQuery.setOfficeList( officeService.findAll());
        dutySignQuery.setPositionList(positionService.findAll());
        return dutySignQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutySignForm dutySignForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("签到成功", null);
        dutySignService.save(dutySignForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutySignService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "getForm")
    public DutySignForm detail(DutySignForm dutySignForm) {
        dutySignForm = dutySignService.getForm(dutySignForm);
        return dutySignForm;
    }

    @RequestMapping(value = "export")
    public ModelAndView export(DutySignQuery dutySignQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        SimpleExcelSheet simpleExcelSheet = dutySignService.findSimpleExcelSheet(workbook,dutySignQuery);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook, "签到列表.xlsx", simpleExcelSheet);
        return null;
    }
}
