package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderExtendTypeEnum;
import net.myspring.future.modules.crm.dto.ExpressDto;
import net.myspring.future.modules.crm.service.ExpressService;
import net.myspring.future.modules.crm.web.form.ExpressForm;
import net.myspring.future.modules.crm.web.query.ExpressQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ExpressDto> list(Pageable pageable, ExpressQuery expressQuery){
        Page<ExpressDto> page = expressService.findPage(pageable, expressQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public ExpressQuery getQuery() {
        ExpressQuery result = new ExpressQuery();
        result.setExpressOrderExtendTypeList(ExpressOrderExtendTypeEnum.getList());
        return result;
    }


    @RequestMapping(value = "save")
    public RestResponse save(ExpressForm expressForm) {
        expressService.saveOrUpdate(expressForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ExpressForm expressForm) {
        expressService.logicDeleteOne(expressForm.getId());
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;

    }

    @RequestMapping(value = "getFormProperty")
    public ExpressForm getFormProperty(ExpressForm expressForm){
        ExpressForm result = expressService.getFormProperty(expressForm);

        return result;
    }


}
