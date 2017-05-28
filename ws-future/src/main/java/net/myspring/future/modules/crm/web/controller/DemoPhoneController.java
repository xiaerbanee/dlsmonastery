package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.service.DemoPhoneTypeService;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.DemoPhoneDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.service.DemoPhoneService;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.form.DemoPhoneForm;
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/demoPhone")
public class DemoPhoneController {

    @Autowired
    private DemoPhoneService demoPhoneService;

    @Autowired
    private DemoPhoneTypeService demoPhoneTypeService;


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
    public String export(DemoPhoneQuery demoPhoneQuery) throws IOException{
        Workbook workbook = new SXSSFWorkbook(10000);
        return demoPhoneService.findSimpleExcelSheets(workbook,demoPhoneQuery);
    }
    private List<String> getActionList(DemoPhone demoPhone) {
        return null;
    }
}
