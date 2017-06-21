package net.myspring.basic.modules.hr.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DutySignValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "error.address", "请选择地址");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "attachment", "error.attachment", "请选择照片");
    }
}
