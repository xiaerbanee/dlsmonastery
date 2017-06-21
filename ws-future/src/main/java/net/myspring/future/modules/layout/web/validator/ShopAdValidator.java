package net.myspring.future.modules.layout.web.validator;

import net.myspring.future.common.enums.ShopAdTotalPriceTypeEnum;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.service.ShopAdTypeService;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Created by admin on 2016/12/27.
 */
@Component
public class ShopAdValidator implements Validator {

    @Autowired
    private ShopAdTypeService shopAdTypeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ShopAdForm shopAd=(ShopAdForm)target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopId", "error.shopId", "门店不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopAdTypeId", "error.shopAdTypeId", "广告品种不能为空");
        if(shopAd.getLength()!=null && shopAd.getLength().compareTo(BigDecimal.ZERO)==-1 ){
            errors.rejectValue("length", "error.qty", "长度不能为小于0");
        }
        if(shopAd.getWidth()!=null && shopAd.getWidth().compareTo(BigDecimal.ZERO)==-1 ){
            errors.rejectValue("width", "error.qty", "高度不能小于0");
        }
        if(shopAd.getQty()!=null && shopAd.getQty()<0 ){
            errors.rejectValue("qty", "error.qty", "数量值不能小于0");
        }
        if(StringUtils.isNotBlank(shopAd.getShopAdTypeId())){
            ShopAdTypeDto shopAdType= shopAdTypeService.findOne(shopAd.getShopAdTypeId());
            if(ShopAdTotalPriceTypeEnum.按数量.name().equals(shopAdType.getTotalPriceType())){
                if(shopAd.getQty()==null){
                    errors.rejectValue("qty", "error.qty", "数量不能为空");
                }
            }
            if(ShopAdTotalPriceTypeEnum.按面积.name().equals(shopAdType.getTotalPriceType())){
                if(shopAd.getLength()==null||shopAd.getWidth()==null||shopAd.getQty()==null){
                    errors.rejectValue("length", "error.qty", "长度不能为空");
                    errors.rejectValue("width", "error.qty", "高度不能为空");
                    errors.rejectValue("qty", "error.qty", "数量值不能为空");
                }
            }
        }
    }
}
