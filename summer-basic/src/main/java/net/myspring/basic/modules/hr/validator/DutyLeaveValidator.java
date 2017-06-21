package net.myspring.basic.modules.hr.validator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyLeave;
import net.myspring.basic.modules.hr.service.DutyLeaveService;
import net.myspring.basic.modules.hr.web.form.DutyLeaveForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import net.myspring.common.utils.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/12.
 */
@Component
public class DutyLeaveValidator implements Validator {

    @Autowired
    private DutyLeaveService dutyLeaveService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        DutyLeaveForm dutyLeave = (DutyLeaveForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateType", "error.dateType", "请选择时间类型");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "leaveType", "error.leaveType", "请选择请假类型");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dutyDateStart", "error.dutyDateStart", "开始日期不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dutyDateEnd", "error.dutyDateEnd", "结束日期不能为空");

        if (StringUtils.isNotBlank(dutyLeave.getDutyDateStart() ) && StringUtils.isNotBlank(dutyLeave.getDutyDateEnd())) {
            String employeeId = RequestUtils.getRequestEntity().getEmployeeId();
            if (dutyLeave.getDutyDateStart().equals(dutyLeave.getDutyDateEnd())) {
                List<DutyLeave> dutyLeaves = dutyLeaveService.findByDutyDate(employeeId, LocalDateUtils.parse(dutyLeave.getDutyDateStart()));
                List<String> dateTypes = CollectionUtil.extractToList(dutyLeaves, "dateType");
                boolean isNotCross = CollectionUtil.isEmpty(dateTypes) || (!DutyDateTypeEnum.全天.name().equals(dutyLeave.getDateType()) && !dateTypes.contains(DutyDateTypeEnum.全天.name()) && !dateTypes.contains(dutyLeave.getDateType()));
                if (!isNotCross) {
                    errors.rejectValue("dutyDateStart", "error.dutyDateStart", "保存失败，请假时间已存在");
                }
            } else {
                LocalDate dateStart = LocalDate.parse(dutyLeave.getDutyDateStart());
                LocalDate dateEnd = LocalDate.parse(dutyLeave.getDutyDateEnd());
                List<LocalDate> dateList = LocalDateUtils.getDateList(dateStart, dateEnd);
                List<DutyLeave> dutyLeaves = dutyLeaveService.findByDutyDateList(employeeId, dateList);
                Map<LocalDate, List<DutyLeave>> dutyLeaveMap = Maps.newHashMap();
                for (DutyLeave leave : dutyLeaves) {
                    if (!dutyLeaveMap.containsKey(leave.getDutyDate())) {
                        dutyLeaveMap.put(leave.getDutyDate(), Lists.newArrayList());
                    }
                    dutyLeaveMap.get(leave.getDutyDate()).add(leave);
                }
                for (LocalDate date : dateList) {
                    List<DutyLeave> dutyLeaveList = dutyLeaveMap.get(date);
                    List<String> dateTypes = CollectionUtil.extractToList(dutyLeaveList, "dateType");
                    boolean isNotCross = CollectionUtil.isEmpty(dateTypes) || (!DutyDateTypeEnum.全天.name().equals(dutyLeave.getDateType()) && !dateTypes.contains(DutyDateTypeEnum.全天.name()) && !dateTypes.contains(dutyLeave.getDateType()));
                    if (!isNotCross) {
                        errors.rejectValue("dutyDateStart", "error.dutyDateStart", "保存失败，请假时间已存在");
                        break;
                    }
                }
            }
        }
    }
}
