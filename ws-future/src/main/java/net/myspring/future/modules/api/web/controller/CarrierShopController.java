package net.myspring.future.modules.api.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.CarrierShopTypeEnum;
import net.myspring.future.modules.api.dto.CarrierShopDto;
import net.myspring.future.modules.api.service.CarrierShopService;
import net.myspring.future.modules.api.web.form.CarrierShopForm;
import net.myspring.future.modules.api.web.query.CarrierShopQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhucc on 2017/7/5.
 */
@RestController
@RequestMapping(value = "api/carrierShop")
public class CarrierShopController {

    @Autowired
    private CarrierShopService carrierShopService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'sys:CarrierShop:view')")
    public Page<CarrierShopDto> list(Pageable pageable, CarrierShopQuery carrierShopQuery){
        Page<CarrierShopDto> page = carrierShopService.findPage(pageable,carrierShopQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        carrierShopService.logicDelete(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(CarrierShopForm carrierShopForm) {
        carrierShopService.save(carrierShopForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public CarrierShopDto findOne(String id){
        return carrierShopService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    public CarrierShopForm getForm(CarrierShopForm CarrierShopForm){
        CarrierShopForm.getExtra().put("typeList",CarrierShopTypeEnum.getTypes());
        return CarrierShopForm;
    }


    @RequestMapping(value="getQuery")
    public  CarrierShopQuery getQuery(CarrierShopQuery CarrierShopQuery){
        CarrierShopQuery.getExtra().put("typeList", CarrierShopTypeEnum.getTypes());
        return CarrierShopQuery;
    }
}
