package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.service.DutyAnnualService;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping(value = "hr/dutyAnnual")
public class DutyAnnualController {

    @Autowired
    private DutyAnnualService dutyAnnualService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyAnnualDto> list(Pageable pageable, DutyAnnualQuery dutyAnnualQuery) {
        dutyAnnualQuery.setCreatedBy(SecurityUtils.getAccountId());
        Page<DutyAnnualDto> page  = dutyAnnualService.findPage(pageable,dutyAnnualQuery);
        return page;
    }

    @RequestMapping(value = "import/template", method = RequestMethod.GET)
    public String impotTemplate() throws IOException {
        Workbook workbook = new SXSSFWorkbook(10000);
        return dutyAnnualService.findSimpleExcelSheet(workbook);
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public RestResponse importFile(@RequestParam(value = "mongoId", required = true) String mongoId, String annualYear, String remarks) {
        dutyAnnualService.save(mongoId, annualYear, remarks);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

}
