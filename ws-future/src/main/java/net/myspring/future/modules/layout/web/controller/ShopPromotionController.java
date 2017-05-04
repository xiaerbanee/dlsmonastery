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

    @RequestMapping(value = "findForm")
    public ShopPromotionForm findOne(ShopPromotionForm shopPromotionForm) {
        shopPromotionForm = shopPromotionService.findForm(shopPromotionForm);
        return shopPromotionForm;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    public List<String> getQuery() {
        return ActivityTypeEnum.getList();
    }

    @RequestMapping(value="save")
    public RestResponse save(ShopPromotionForm shopPromotionForm) {
        shopPromotionService.save(shopPromotionForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="detail")
    public String detail() {
        return null;
    }

    @RequestMapping(value="delete")
    public RestResponse delete(ShopPromotionForm shopPromotionForm) {
        shopPromotionService.logicDeleteOne(shopPromotionForm.getId());
        return new RestResponse("delete success", ResponseCodeEnum.removed.name());
    }

    private List<String> getActionList() {
        return null;
    }
}
