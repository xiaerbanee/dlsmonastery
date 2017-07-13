package net.myspring.future.modules.layout.web.validator;

import net.myspring.future.modules.layout.service.ShopImageService;
import net.myspring.future.modules.layout.web.form.ShopImageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Created by zhucc on 2017/7/13.
 */
@Component
public class ShopImageValidator implements Validator{


    @Override
    public boolean supports(Class<?> clazz){return false;}

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopId", "error.shopId", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "imageType", "error.imageType", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "imageSize", "error.imageSize", "必填信息");
    }

}
