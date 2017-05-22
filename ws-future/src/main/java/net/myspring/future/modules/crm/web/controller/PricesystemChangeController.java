package net.myspring.future.modules.crm.web.controller;


import com.google.common.collect.Lists;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.service.PricesystemService;
import net.myspring.future.modules.crm.domain.PricesystemChange;

import net.myspring.future.modules.crm.dto.PricesystemChangeDto;
import net.myspring.future.modules.crm.service.PricesystemChangeService;
import net.myspring.future.modules.crm.web.form.PricesystemChangeForm;
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/pricesystemChange")
public class PricesystemChangeController {

    @Autowired
    private PricesystemChangeService pricesystemChangeService;

    @Autowired
    private PricesystemService pricesystemService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<PricesystemChangeDto> list(Pageable pageable,PricesystemChangeQuery pricesystemChangeQuery) {
        Page<PricesystemChangeDto> page=pricesystemChangeService.findPage(pageable,pricesystemChangeQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public PricesystemChangeQuery getQuery(PricesystemChangeQuery pricesystemChangeQuery){
        pricesystemChangeQuery.setStatusList(pricesystemChangeService.findStatus());
        pricesystemChangeQuery.setPricesystems(pricesystemService.findPricesystem());
        return pricesystemChangeQuery;
    }

    @RequestMapping(value="getForm")
    public String getForm(){
        return null;
    }

    @RequestMapping(value = "save")
    public RestResponse save(PricesystemChangeForm pricesystemChangeForm, BindingResult bindingResult) {
         pricesystemChangeService.save(pricesystemChangeForm);
        return new RestResponse("保存成功",null );
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(@RequestParam(value = "ids[]") String[] ids, Boolean pass){
        pricesystemChangeService.audit(ids, pass);
        return new RestResponse("通过成功",null);
    }

    @RequestMapping(value="auditOperation")
    public RestResponse auditOperation(String id,Boolean pass){
        pricesystemChangeService.auditOperation(id, pass);
        return new RestResponse("设置成功",null);
    }

    @RequestMapping(value = "getPricesystemDetail")
    public String getPricesystemDetail(@RequestParam(value = "productIdList[]") String[] proudctIdList){
        return null;
    }

    private List<String> getActionList(PricesystemChange pricesystemChange) {
        return null;
    }
}
