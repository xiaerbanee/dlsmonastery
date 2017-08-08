package net.myspring.basic.modules.hr.web.validator;

import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmployeeValidator implements Validator{

    @Override
    public boolean supports(Class<?>clazz){return false;}

    @Override
    public void validate(Object target, Errors errors){
        EmployeeForm employeeForm = (EmployeeForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "salary", "error.salary", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobilePhone", "error.mobilePhone", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "idcard", "error.idcard", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entryDate", "error.entryDate", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountForm.loginName", "error.loginName", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountForm.officeId", "error.officeId", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountForm.positionId", "error.positionId", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountForm.officeIdList", "error.officeIdList", "必填信息");
    }
};
