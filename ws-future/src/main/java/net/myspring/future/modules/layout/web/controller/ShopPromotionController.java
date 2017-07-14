package net.myspring.future.modules.layout.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ActivityTypeEnum;
import net.myspring.future.modules.layout.dto.ShopPromotionDto;
import net.myspring.future.modules.layout.service.ShopPromotionService;
import net.myspring.future.modules.layout.web.form.ShopPromotionForm;
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery;
import net.myspring.future.modules.layout.web.validator.ShopPromotionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "layout/shopPromotion")
public class ShopPromotionController {

    @Autowired
    private ShopPromotionService shopPromotionService;
    @Autowired
    private ShopPromotionValidator shopPromotionValidator;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopPromotion:view')")
    public Page<ShopPromotionDto> list(Pageable pageable, ShopPromotionQuery shopPromotionQuery) {
        return shopPromotionService.findPage(pageable,shopPromotionQuery);
   }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:shopPromotion:view')")
    public ShopPromotionForm getForm(ShopPromotionForm shopPromotionForm) {
        return shopPromotionService.getForm(shopPromotionForm);
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:shopPromotion:view')")
    public ShopPromotionDto findOne(String id){
        return  shopPromotionService.findOne(id);
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:shopPromotion:view')")
    public ShopPromotionQuery getQuery(ShopPromotionQuery shopPromotionQuery) {
        shopPromotionQuery.getExtra().put("activityTypeList",ActivityTypeEnum.getList());
        return shopPromotionQuery;
    }

    @RequestMapping(value="save")
    @PreAuthorize("hasPermission(null,'crm:shopPromotion:edit')")
    public RestResponse save(ShopPromotionForm shopPromotionForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        shopPromotionValidator.validate(shopPromotionForm,bindingResult);
        if(bindingResult.hasErrors()){
            return  new RestResponse(bindingResult,"保存失败", ResponseCodeEnum.saved.name());
        }
        shopPromotionService.save(shopPromotionForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="delete")
    @PreAuthorize("hasPermission(null,'crm:shopPromotion:delete')")
    public RestResponse delete(ShopPromotionForm shopPromotionForm) {
        shopPromotionService.logicDelete(shopPromotionForm.getId());
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

}
