package net.myspring.basic.modules.hr.web.validator;

import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz){return false;}

    @Override
    public void validate(Object target, Errors errors){
        AccountForm accountForm = (AccountForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginName", "error.loginName", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "officeId", "error.officeId", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "positionId", "error.positionId", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "officeIdList", "error.officeIdList", "必填信息");
    }
}
