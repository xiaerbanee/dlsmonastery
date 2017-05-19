package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.service.ExpressOrderService;
import net.myspring.future.modules.crm.web.form.ExpressOrderForm;
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "crm/expressOrder")
public class ExpressOrderController {

    @Autowired
    private ExpressOrderService expressOrderService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ExpressOrderDto> list(Pageable pageable, ExpressOrderQuery expressOrderQuery){
        Page<ExpressOrderDto> page = expressOrderService.findPage(pageable, expressOrderQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public ExpressOrderQuery getQuery() {
        ExpressOrderQuery result = new ExpressOrderQuery();
        result.setExtendTypeList(ExpressOrderTypeEnum.getList());
        return result;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ExpressOrderForm expressOrderForm){
        expressOrderService.save(expressOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "resetPrintStatus")
    public RestResponse resetPrintStatus(String id) {

        expressOrderService.resetPrintStatus(id);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "getForm")
    public ExpressOrderForm getForm(ExpressOrderForm expressOrderForm){
        ExpressOrderForm result = expressOrderService.getForm(expressOrderForm);
        return result;
    }

    @RequestMapping(value = "exportEMS", method = RequestMethod.GET)
    public String exportEMS(ExpressOrderQuery expressOrderQuery) throws IOException {
        return expressOrderService.genEMSDataFileForExport(expressOrderQuery);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(ExpressOrderQuery expressOrderQuery) throws IOException {
        return expressOrderService.genDataFileForExport(expressOrderQuery);
    }
}
