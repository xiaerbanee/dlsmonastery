package net.myspring.future.modules.layout.web.controller;


import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.modules.layout.dto.ShopImageDto;
import net.myspring.future.modules.layout.service.ShopImageService;
import net.myspring.future.modules.layout.web.form.ShopImageForm;
import net.myspring.future.modules.layout.web.query.ShopImageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "layout/shopImage")
public class ShopImageController {

    @Autowired
    private ShopImageService shopImageService;
    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopImageDto> list(Pageable pageable, ShopImageQuery shopImageQuery) {

        return shopImageService.findPage(pageable,shopImageQuery);
    }

    @RequestMapping(value = "findOne")
    public ShopImageForm findOne(ShopImageForm shopImageForm) {
        shopImageForm = shopImageService.getFormProperty(shopImageForm);
        List<String> imageTypeList=Arrays.asList(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.SHOP_IMAGE_TYPE.name()).getValue().split(","));
        shopImageForm.setImageTypeList(imageTypeList);
        return shopImageForm;
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public ShopImageQuery getQuery(ShopImageQuery shopImageQuery) {
        return shopImageService.getQuery(shopImageQuery);
    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopImageForm shopImageForm) {
        shopImageService.save(shopImageForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse logicDelete(ShopImageForm shopImageForm) {
        shopImageService.logicDelete(shopImageForm.getId());
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    public List<String> getActionList() {
        return null;
    }

}
