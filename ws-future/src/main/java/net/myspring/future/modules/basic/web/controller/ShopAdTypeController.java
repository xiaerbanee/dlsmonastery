package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.TotalPriceTypeEnum;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.service.ShopAdTypeService;
import net.myspring.future.modules.basic.web.Query.ShopAdTypeQuery;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/shopAdType")
public class ShopAdTypeController {

    @Autowired
    private ShopAdTypeService shopAdTypeService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopAdTypeDto> list(Pageable pageable, ShopAdTypeQuery shopAdTypeQuery){
        Page<ShopAdTypeDto> page = shopAdTypeService.findPage(pageable,shopAdTypeQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ShopAdTypeForm shopAdTypeForm) {
        shopAdTypeService.delete(shopAdTypeForm);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopAdTypeForm shopAdTypeForm) {
        shopAdTypeService.save(shopAdTypeForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public ShopAdTypeForm findOne(ShopAdTypeForm shopAdTypeForm){
        shopAdTypeForm=shopAdTypeService.findForm(shopAdTypeForm);
        shopAdTypeForm.setTotalPriceTypeList(TotalPriceTypeEnum.getValues());
       return shopAdTypeForm;
    }

    @RequestMapping(value="getListProperty")
    public ShopAdTypeQuery getListProperty(ShopAdTypeQuery shopAdTypeQuery){
        shopAdTypeQuery.setTotalPriceTypeList(TotalPriceTypeEnum.getValues());
        return shopAdTypeQuery;
    }

}