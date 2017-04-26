package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery;
import net.myspring.future.modules.basic.web.form.ExpressCompanyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "basic/expressCompany")
public class ExpressCompanyController {

    @Autowired
    private ExpressCompanyService expressCompanyService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ExpressCompanyDto> list(Pageable pageable, ExpressCompanyQuery expressCompanyQuery){
        Page<ExpressCompanyDto> page = expressCompanyService.findPage(pageable,expressCompanyQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ExpressCompanyForm expressCompanyForm) {
        expressCompanyService.delete(expressCompanyForm);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ExpressCompanyForm expressCompanyForm) {
        expressCompanyService.save(expressCompanyForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findForm")
    public ExpressCompanyForm findOne(ExpressCompanyForm expressCompanyForm){
        expressCompanyForm=expressCompanyService.findForm(expressCompanyForm);
        expressCompanyForm.setExpressTypeList(ExpressCompanyTypeEnum.getList());
        return expressCompanyForm;
    }
    @RequestMapping(value="getQuery")
    public  ExpressCompanyQuery getQuery(ExpressCompanyQuery expressCompanyQuery){
        expressCompanyQuery.setExpressTypeList(expressCompanyService.findExpressTypeList());
        return expressCompanyQuery;
    }
}
