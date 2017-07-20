package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.TotalPriceTypeEnum;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.service.ShopAdTypeService;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import net.myspring.future.modules.basic.web.query.ShopAdTypeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "basic/shopAdType")
public class ShopAdTypeController {

    @Autowired
    private ShopAdTypeService shopAdTypeService;


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopAdType:view')")
    public Page<ShopAdTypeDto> list(Pageable pageable, ShopAdTypeQuery shopAdTypeQuery){
        return  shopAdTypeService.findPage(pageable, shopAdTypeQuery);
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:shopAdType:delete')")
    public RestResponse delete(ShopAdTypeForm shopAdTypeForm) {
        shopAdTypeService.delete(shopAdTypeForm);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:shopAdType:edit')")
    public RestResponse save(ShopAdTypeForm shopAdTypeForm) {
        shopAdTypeService.save(shopAdTypeForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:shopAdType:view')")
    public ShopAdTypeDto findOne(String id){
        return shopAdTypeService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:shopAdType:view')")
    public ShopAdTypeForm getForm(ShopAdTypeForm shopAdTypeForm){
       return shopAdTypeService.getForm(shopAdTypeForm);
    }

    @RequestMapping(value="getQuery")
    @PreAuthorize("hasPermission(null,'crm:shopAdType:view')")
    public ShopAdTypeQuery getQuery(ShopAdTypeQuery shopAdTypeQuery){
        return shopAdTypeService.getQuery(shopAdTypeQuery);
    }

}
