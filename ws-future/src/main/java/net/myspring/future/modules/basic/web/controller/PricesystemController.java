package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.service.PricesystemService;
import net.myspring.future.modules.basic.web.query.PricesystemQuery;
import net.myspring.future.modules.basic.web.form.PricesystemForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "crm/pricesystem")
public class PricesystemController {

    @Autowired
    private PricesystemService pricesystemService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<PricesystemDto> list(Pageable pageable, PricesystemQuery pricesystemQuery) {
        Page<PricesystemDto> page = pricesystemService.findPage(pageable, pricesystemQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(PricesystemForm pricesystemForm) {
        pricesystemService.logicDelete(pricesystemForm);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(PricesystemForm pricesystemForm) {
        pricesystemService.save(pricesystemForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    public PricesystemForm findOne(PricesystemForm pricesystemForm) {
        pricesystemForm=pricesystemService.getForm(pricesystemForm);
        return pricesystemForm;
    }

}
