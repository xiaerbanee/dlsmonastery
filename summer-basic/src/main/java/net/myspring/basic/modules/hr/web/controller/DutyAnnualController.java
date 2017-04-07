package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.config.ExcelView;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.service.DutyAnnualService;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.basic.modules.sys.service.FolderFileService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/dutyAnnual")
public class DutyAnnualController {

    @Autowired
    private DutyAnnualService dutyAnnualService;
    @Autowired
    private FolderFileService folderFileService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyAnnualDto> list(Pageable pageable, DutyAnnualQuery dutyAnnualQuery) {
        dutyAnnualQuery.setCreatedBy(securityUtils.getAccountId());
        Page<DutyAnnualDto> page  = dutyAnnualService.findPage(pageable,dutyAnnualQuery);
        return page;
    }

    @RequestMapping(value = "getListProperty")
    public Map<String,Object> getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        return map;
    }

    @RequestMapping(value = "import/template", method = RequestMethod.GET)
    public ModelAndView impotTemplate() {
        Workbook workbook = new SXSSFWorkbook(10000);
        SimpleExcelSheet simpleExcelSheet=dutyAnnualService.findSimpleExcelSheet(workbook);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"年假导入模版.xlsx",simpleExcelSheet);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public RestResponse importFile(@RequestParam(value = "folderFileId", required = true) String folderFileId, String annualYear, String remarks) {
        FolderFile folderFile = folderFileService.findOne(folderFileId);
        File file  = new File(folderFileService.getUploadPath(folderFile));
        dutyAnnualService.save(file, annualYear, remarks);
        RestResponse restResponse=new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        return restResponse;
    }

}
