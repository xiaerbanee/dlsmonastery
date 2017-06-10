package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ActivityTypeEnum;
import net.myspring.future.modules.layout.dto.ShopPromotionDto;
import net.myspring.future.modules.layout.service.ShopPromotionService;
import net.myspring.future.modules.layout.web.form.ShopPromotionForm;
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "layout/shopPromotion")
public class ShopPromotionController {

    @Autowired
    private ShopPromotionService shopPromotionService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopPromotionDto> list(Pageable pageable, ShopPromotionQuery shopPromotionQuery) {
        return shopPromotionService.findPage(pageable,shopPromotionQuery);
   }

    @RequestMapping(value = "getForm")
    public ShopPromotionForm getForm(ShopPromotionForm shopPromotionForm) {
        return shopPromotionService.getForm(shopPromotionForm);
    }

    @RequestMapping(value = "findOne")
    public ShopPromotionDto findOne(String id){
        return  shopPromotionService.findOne(id);
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    public ShopPromotionQuery getQuery(ShopPromotionQuery shopPromotionQuery) {
        shopPromotionQuery.getExtra().put("activityTypeList",ActivityTypeEnum.getList());
        return shopPromotionQuery;
    }

    @RequestMapping(value="save")
    public RestResponse save(ShopPromotionForm shopPromotionForm) {
        shopPromotionService.save(shopPromotionForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="delete")
    public RestResponse delete(ShopPromotionForm shopPromotionForm) {
        shopPromotionService.logicDelete(shopPromotionForm.getId());
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

}
