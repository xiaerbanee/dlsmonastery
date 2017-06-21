package net.myspring.future.modules.layout.web.validator;

import net.myspring.future.common.enums.ShopAdTotalPriceTypeEnum;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.service.ShopAdTypeService;
import net.myspring.future.modules.layout.domain.ShopAd;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

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
        ShopAdForm shopAdForm=(ShopAdForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopId", "error.shopId", "门店不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopAdTypeId", "error.shopAdTypeId", "广告品种不能为空");
        if(shopAdForm.getLength()!=null && shopAdForm.getLength().compareTo(BigDecimal.ZERO)==-1 ){
            errors.rejectValue("length", "error.qty", "长度不能为小于0");
        }
        if(shopAdForm.getWidth()!=null && shopAdForm.getWidth().compareTo(BigDecimal.ZERO)==-1 ){
            errors.rejectValue("width", "error.qty", "高度不能小于0");
        }
        if(shopAdForm.getQty()!=null && shopAdForm.getQty()<0 ){
            errors.rejectValue("qty", "error.qty", "数量值不能小于0");
        }
        if(StringUtils.isNotBlank(shopAdForm.getShopAdTypeId())){
            ShopAdTypeDto shopAdTypeDto= shopAdTypeService.findOne(shopAdForm.getShopAdTypeId());
            if(ShopAdTotalPriceTypeEnum.按数量.name().equals(shopAdTypeDto.getTotalPriceType())){
                if(shopAdForm.getQty()==null){
                    errors.rejectValue("qty", "error.qty", "数量不能为空");
                }
            }
            if(ShopAdTotalPriceTypeEnum.按面积.name().equals(shopAdTypeDto.getTotalPriceType())){
                if(shopAdForm.getLength()==null||shopAdForm.getWidth()==null||shopAdForm.getQty()==null){
                    errors.rejectValue("length", "error.qty", "长度不能为空");
                    errors.rejectValue("width", "error.qty", "高度不能为空");
                    errors.rejectValue("qty", "error.qty", "数量值不能为空");
                }
            }
        }
    }
}
