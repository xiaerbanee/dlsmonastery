package net.myspring.basic.modules.hr.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by liuj on 2016/12/10.
 */
@Component
public class DutySignValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "attachment", "error.attachment", "请选择照片");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "error.address", "请选择地址");

    }
}
