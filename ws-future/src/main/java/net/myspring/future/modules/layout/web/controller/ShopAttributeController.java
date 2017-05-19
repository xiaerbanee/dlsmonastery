package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.layout.service.ShopAttributeService;
import net.myspring.future.modules.layout.web.form.ShopAttributeForm;
import net.myspring.future.modules.layout.web.query.ShopAttributeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "layout/shopAttribute")
public class ShopAttributeController {

    @Autowired
    private ShopAttributeService shopAttributeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotDto> list(Pageable pageable, ShopAttributeQuery shopAttributeQuery){
        Page<DepotDto> page = shopAttributeService.findPage(pageable, shopAttributeQuery);
        return page;
    }

    @RequestMapping(value = "getFormProperty")
    public ShopAttributeForm getFormProperty(ShopAttributeForm shopAttributeForm){
        shopAttributeForm=shopAttributeService.getFormProperty(shopAttributeForm);
        return shopAttributeForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopAttributeForm shopAttributeForm){
        RestResponse restResponse = shopAttributeService.check(shopAttributeForm);
        if(!restResponse.getSuccess()) {
            return restResponse;
        }
        shopAttributeService.save(shopAttributeForm);
        return new RestResponse("保存成功",null);
    }

}
