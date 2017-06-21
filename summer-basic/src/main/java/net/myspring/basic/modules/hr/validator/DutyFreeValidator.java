package net.myspring.basic.modules.hr.validator;

import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.modules.hr.service.DutyFreeService;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by admin on 2016/12/12.
 */
@Component
public class DutyFreeValidator implements Validator {

    @Autowired
    private DutyFreeService dutyFreeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        DutyFree dutyFree = (DutyFree) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateType", "error.dateType", "请选择时间类型");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "freeDate", "error.freeDate", "请选择时间");

        if (dutyFree.getDateType() != null && dutyFree.getFreeDate() != null) {
            if (ChronoUnit.DAYS.between(dutyFree.getFreeDate(), LocalDateTime.now()) > 10) {
                errors.rejectValue("freeDate", "error.freeDate", "只能申请10天内数据");
            }
            List<DutyFree> freeList = dutyFreeService.findByDate(dutyFree.getFreeDate(), RequestUtils.getRequestEntity().getEmployeeId());
            List<String> dateTypes = CollectionUtil.extractToList(freeList, "dateType");
            boolean isNotCross = CollectionUtil.isEmpty(dateTypes) || (!DutyDateTypeEnum.全天.name().equals(dutyFree.getDateType()) && !dateTypes.contains(DutyDateTypeEnum.全天.name()) && !dateTypes.contains(dutyFree.getDateType()));
            if (!isNotCross) {
                errors.rejectValue("freeDate", "error.freeDate", "申请时间已存在，无法申请");
            }
        }
    }

}
