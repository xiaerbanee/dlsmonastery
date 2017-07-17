package net.myspring.future.modules.crm.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.annotation.Target;

/**
 * Created by zhucc on 2017/7/17.
 */
@Component
public class ImeAllotValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz){return false;}

    @Override
    public void validate(Object target ,Errors errors){
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "imeStr", "error.imeStr", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDepotId", "error.toDepotId", "必填信息");

    }
}
