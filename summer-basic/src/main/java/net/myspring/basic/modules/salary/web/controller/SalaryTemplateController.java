package net.myspring.basic.modules.salary.web.controller;

import net.myspring.basic.modules.salary.service.SalaryTemplateService;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "hr/salaryTemplate")
public class SalaryTemplateController {

    @Autowired
    private SalaryTemplateService salaryTemplateService;

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView impotTemplate(String id) {
        SimpleExcelBook simpleExcelSheet = salaryTemplateService.findSimpleExcelSheet(id);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelSheet);
    }
}
