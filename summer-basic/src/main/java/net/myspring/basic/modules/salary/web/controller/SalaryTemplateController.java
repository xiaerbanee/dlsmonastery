package net.myspring.basic.modules.salary.web.controller;

import net.myspring.basic.modules.salary.dto.SalaryTemplateDto;
import net.myspring.basic.modules.salary.service.SalaryTemplateService;
import net.myspring.basic.modules.salary.web.form.SalaryTemplateForm;
import net.myspring.basic.modules.salary.web.query.SalaryTemplateQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "salary/salaryTemplate")
public class SalaryTemplateController {

    @Autowired
    private SalaryTemplateService salaryTemplateService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<SalaryTemplateDto> list(Pageable pageable,SalaryTemplateQuery salaryTemplateQuery) {
        Page<SalaryTemplateDto> page=salaryTemplateService.findPage(pageable,salaryTemplateQuery);
        return page;
    }

    @RequestMapping(value = "findOne", method = RequestMethod.GET)
    public SalaryTemplateDto findOne(SalaryTemplateDto salaryTemplateDto) {
        salaryTemplateDto=salaryTemplateService.findOne(salaryTemplateDto);
        return salaryTemplateDto;
    }

    @RequestMapping(value="getQuery",method=RequestMethod.GET)
    public SalaryTemplateQuery getQuery(SalaryTemplateQuery salaryTemplateQuery){
        return salaryTemplateQuery;
    }
    @RequestMapping(value = "getForm", method = RequestMethod.GET)
    public SalaryTemplateForm getForm(SalaryTemplateForm salaryTemplateForm) {
        return salaryTemplateForm;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(SalaryTemplateForm salaryTemplateForm) {
        salaryTemplateService.save(salaryTemplateForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(String id) {
        salaryTemplateService.delete(id);
        return new RestResponse("删除成功",null);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView impotTemplate(String id) {
        SimpleExcelBook simpleExcelSheet = salaryTemplateService.findSimpleExcelSheet(id);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelSheet);
    }
}
