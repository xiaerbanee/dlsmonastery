package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.service.PriceChangeService;
import net.myspring.future.modules.crm.web.form.PriceChangeForm;
import net.myspring.future.modules.crm.web.query.PriceChangeQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/priceChange")
public class PriceChangeController {

    @Autowired
    private PriceChangeService priceChangeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<PriceChangeDto> list(Pageable pageable,PriceChangeQuery priceChangeQuery){
        return priceChangeService.findPage(pageable,priceChangeQuery);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(PriceChangeForm priceChangeForm) {
        priceChangeService.delete(priceChangeForm);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    public RestResponse save(PriceChangeForm priceChangeForm) {
        priceChangeService.save(priceChangeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "check")
    public RestResponse check(PriceChangeForm priceChangeForm) {
        priceChangeService.check(priceChangeForm);
        return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "findOne")
    public PriceChangeDto findOne(String id){
        return priceChangeService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    public PriceChangeForm getForm(PriceChangeForm priceChangeForm){
        return priceChangeService.getForm(priceChangeForm);
    }

    @RequestMapping(value="getQuery")
    public PriceChangeQuery getQuery(PriceChangeQuery priceChangeQuery){
        return priceChangeQuery;
    }

}
