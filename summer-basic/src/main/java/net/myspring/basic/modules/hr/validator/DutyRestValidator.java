package net.myspring.basic.modules.hr.validator;

import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by admin on 2016/12/19.
 */
@Component
public class DutyRestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz){
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        DutyRestForm dutyRest = (DutyRestForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "error.type", "请选择调休类型");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dutyDate", "error.dutyDate", "请选择日期");

        if(dutyRest.getType() !=null ){
            if (dutyRest.getType() .equals( "加班调休")) {
                if(dutyRest.getHour()!=null&&dutyRest.getHour().doubleValue()>dutyRest.getOvertimeLeftHour()){
                    errors.rejectValue( "hour", "error.hour", "时长大于可调休时间");
                }
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeStart", "error.timeStart", "请选择开始时间");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeEnd", "error.timeEnd", "请选择结束日期");
            }
            if (dutyRest.getType() .equals( "年假调休")) {
                if(dutyRest.getHour()!=null&&dutyRest.getHour().doubleValue()>dutyRest.getAnnualLeftHour()){
                    errors.rejectValue("hour", "error.hour", "时长大于可调休时间");
                }
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateType", "error.dateType", "请选择时间类型");
            }
        }
    }
}
