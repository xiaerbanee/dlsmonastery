package net.myspring.future.modules.crm.web;

import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by zhucc on 2017/7/15.
 */
@Component
public class PriceChangeImeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz){return false;}

    @Override
    public void validate(Object target,Errors errors ){
        PriceChangeImeForm priceChangeImeForm=(PriceChangeImeForm)target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "image", "error.image", "必填信息");

    }


}
