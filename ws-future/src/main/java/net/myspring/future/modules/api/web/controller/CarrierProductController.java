package net.myspring.future.modules.api.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.CarrierShopTypeEnum;
import net.myspring.future.modules.api.domain.CarrierProduct;
import net.myspring.future.modules.api.dto.CarrierProductDto;
import net.myspring.future.modules.api.dto.CarrierShopDto;
import net.myspring.future.modules.api.service.CarrierProductService;
import net.myspring.future.modules.api.service.CarrierShopService;
import net.myspring.future.modules.api.web.form.CarrierProductForm;
import net.myspring.future.modules.api.web.form.CarrierShopForm;
import net.myspring.future.modules.api.web.query.CarrierProductQuery;
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
@RequestMapping(value = "api/carrierProduct")
public class CarrierProductController {

    @Autowired
    private CarrierProductService carrierProductService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<CarrierProductDto> list(Pageable pageable, CarrierProductQuery carrierProductQuery){
        Page<CarrierProductDto> page = carrierProductService.findPage(pageable,carrierProductQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        carrierProductService.logicDelete(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(CarrierProductForm carrierProductForm) {
        carrierProductService.save(carrierProductForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public CarrierProductDto findOne(String id){
        return carrierProductService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    public CarrierProductForm getForm(CarrierProductForm carrierProductForm){
        return carrierProductForm;
    }

    @RequestMapping(value="getQuery")
    public  CarrierProductQuery getQuery(CarrierProductQuery carrierProductQuery){
        return carrierProductQuery;
    }

    @RequestMapping(value = "checkName")
    public boolean checkName(CarrierProductForm carrierProductForm) {
        CarrierProduct carrierProduct = carrierProductService.findByName(carrierProductForm.getName());
        return carrierProduct == null ||carrierProduct.getId().equals(carrierProductForm.getId());
    }
}
