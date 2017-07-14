package net.myspring.future.modules.layout.web.validator;

import net.myspring.common.utils.ValidationUtils;
import net.myspring.future.modules.layout.service.ShopPromotionService;
import net.myspring.future.modules.layout.web.form.ShopPromotionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by zhucc on 2017/7/13.
 */
@Component
public class ShopPromotionValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz){return false;}

    @Override
    public void validate(Object target, Errors errors){
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopId", "error.shopId", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "activityType", "error.activityType", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dayAmount", "error.dayAmount", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "error.amount", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "error.phone", "必填信息");
    }

}