package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.service.DutyAnnualService;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping(value = "hr/dutyAnnual")
public class DutyAnnualController {

    @Autowired
    private DutyAnnualService dutyAnnualService;

    @RequestMapping(value = "getQuery")
    public DutyAnnualQuery getQuery(DutyAnnualQuery dutyAnnualQuery) {
        return dutyAnnualQuery;
    }
    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyAnnualDto> list(Pageable pageable, DutyAnnualQuery dutyAnnualQuery) {
        dutyAnnualQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyAnnualDto> page  = dutyAnnualService.findPage(pageable,dutyAnnualQuery);
        return page;
    }

    @RequestMapping(value = "import/template", method = RequestMethod.GET)
    public ModelAndView impotTemplate() throws IOException {
        SimpleExcelBook simpleExcelSheet = dutyAnnualService.findSimpleExcelSheet();
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelSheet);
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public RestResponse importFile(@RequestParam(value = "folderFileId", required = true) String folderFileId, String annualYear, String remarks) {
        dutyAnnualService.save(folderFileId, annualYear, remarks);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

}
