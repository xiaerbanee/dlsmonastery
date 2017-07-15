package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.future.modules.crm.dto.DemoPhoneDto;
import net.myspring.future.modules.crm.service.DemoPhoneService;
import net.myspring.future.modules.crm.web.form.DemoPhoneForm;
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery;
import net.myspring.util.excel.ExcelView;
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
@RequestMapping(value = "crm/demoPhone")
public class DemoPhoneController {

    @Autowired
    private DemoPhoneService demoPhoneService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<DemoPhoneDto> list(Pageable pageable, DemoPhoneQuery demoPhoneQuery) {
        Page<DemoPhoneDto> page = demoPhoneService.findPage(pageable,demoPhoneQuery);
        return page;
    }

    @RequestMapping(value = "findOne")
    public DemoPhoneDto findOne(DemoPhoneDto demoPhoneDto){
        return demoPhoneService.findOne(demoPhoneDto);
    }

    @RequestMapping(value = "getQuery")
    public DemoPhoneQuery getQuery(DemoPhoneQuery demoPhoneQuery){
        return demoPhoneQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DemoPhoneForm demoPhoneForm){
        demoPhoneService.save(demoPhoneForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    public DemoPhoneForm getForm(DemoPhoneForm demoPhoneForm){
        return demoPhoneForm;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        demoPhoneService.delete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(DemoPhoneQuery demoPhoneQuery) throws IOException{
        return new ModelAndView(new ExcelView(),"simpleExcelBook",demoPhoneService.export(demoPhoneQuery));
    }
}
