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


    @ModelAttribute
    public PriceChange get(@RequestParam(required = false) String id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PriceChangeDto> list(Pageable pageable,PriceChangeQuery priceChangeQuery){
        return priceChangeService.findPage(pageable,priceChangeQuery);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(PriceChangeForm priceChangeForm) {
        priceChangeService.delete(priceChangeForm);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(PriceChangeForm priceChangeForm) {
        priceChangeService.save(priceChangeForm);
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }


    @RequestMapping(value = "check")
    public RestResponse check(PriceChangeForm priceChangeForm) {
        priceChangeService.check(priceChangeForm);
        RestResponse restResponse = new RestResponse("审核成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "findOne")
    public PriceChangeDto detail(String id){
        return priceChangeService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    public PriceChangeForm getForm(PriceChangeForm priceChangeForm){
        priceChangeForm = priceChangeService.getForm(priceChangeForm);
        return priceChangeForm;
    }

    @RequestMapping(value="getQuery")
    public PriceChangeQuery getQuery(PriceChangeQuery priceChangeQuery){
        return priceChangeQuery;
    }

    private List<String> getActionList(PriceChange priceChange) {
        return null;
    }

}
