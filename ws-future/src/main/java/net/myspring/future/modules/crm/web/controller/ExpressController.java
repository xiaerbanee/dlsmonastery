package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.modules.crm.dto.ExpressDto;
import net.myspring.future.modules.crm.service.ExpressService;
import net.myspring.future.modules.crm.web.form.ExpressForm;
import net.myspring.future.modules.crm.web.query.ExpressQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping(value = "crm/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ExpressDto> list(Pageable pageable, ExpressQuery expressQuery){
        return expressService.findPage(pageable, expressQuery);
    }

    @RequestMapping(value = "getQuery")
    public ExpressQuery getQuery(ExpressQuery expressQuery) {
        expressQuery.getExtra().put("expressOrderExtendTypeList",ExpressOrderTypeEnum.getList());
        return expressQuery;
    }


    @RequestMapping(value = "save")
    public RestResponse save(ExpressForm expressForm) {
        expressService.save(expressForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        expressService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());

    }

    @RequestMapping(value = "findDto")
    public ExpressDto findDto(String id){

        if(StringUtils.isBlank(id)){
            return new ExpressDto();
        }

        return expressService.findDto(id);

    }

    @RequestMapping(value="export",method = RequestMethod.GET)
    public ModelAndView export(ExpressQuery expressQuery) throws IOException {
        SimpleExcelBook simpleExcelBook = expressService.export(expressQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

    @RequestMapping(value="getForm")
    public ExpressForm getForm(ExpressForm expressForm) {
        return expressForm;
    }
}
