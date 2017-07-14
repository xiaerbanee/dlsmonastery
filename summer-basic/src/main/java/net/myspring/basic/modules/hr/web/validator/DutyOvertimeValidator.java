package net.myspring.basic.modules.hr.web.validator;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyOvertime;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.web.form.DutyOvertimeForm;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/12/14.
 */
@Component
public class DutyOvertimeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Autowired
    private DutyOvertimeService dutyOvertimeService;

    @Override
    public void validate(Object target, Errors errors) {
        DutyOvertimeForm dutyOvertime = (DutyOvertimeForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dutyDate", "error.dutyDate", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeStart", "error.timeStart", "必填信息");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeEnd", "error.timeEnd", "必填信息");
        if (dutyOvertime.getDutyDate() != null && dutyOvertime.getTimeStart() != null && dutyOvertime.getTimeEnd() != null) {
            if (dutyOvertime.getHour() < 2.0) {
                errors.rejectValue("timeStart", "error.timeStart", "加班时间不得少于2小时");
                errors.rejectValue("timeEnd", "error.timeEnd", "加班时间不得少于2小时");
            }
            if (dutyOvertime.getTimeEnd().isBefore(dutyOvertime.getTimeStart())) {
                errors.rejectValue("timeStart", "error.timeStart", "开始时间必须大于结束时间");
            }
            if (LocalDate.now().toEpochDay() - dutyOvertime.getDutyDate().toEpochDay() > 10) {
                errors.rejectValue("dutyDate", "error.dutyDate", "加班日期最早只能补填10天内的数据");
            }
            List<DutyOvertime> dutyOvertimes=dutyOvertimeService.findByDutyDate(RequestUtils.getEmployeeId(),dutyOvertime.getDutyDate());
            boolean isCreate= StringUtils.isBlank(dutyOvertime.getId());
            if (CollectionUtil.isNotEmpty(dutyOvertimes)) {
                for (DutyOvertime overtime : dutyOvertimes) {
                    if (isCreate || !dutyOvertime.getId().equals(overtime.getId())) {
                        if (!AuditTypeEnum.NOT_PASS.getValue().equals(overtime.getStatus())) {
                            if (LocalTimeUtils.isCross(dutyOvertime.getTimeStart(), dutyOvertime.getTimeEnd(), overtime.getTimeStart(), overtime.getTimeEnd())) {
                                errors.rejectValue("timeStart", "error.timeStart", "该时间段内已存在加班申请");
                            }
                        }
                    }
                }
            }
        }
    }
}
