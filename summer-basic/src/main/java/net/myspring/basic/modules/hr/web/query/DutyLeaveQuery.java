package net.myspring.basic.modules.hr.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyLeaveQuery extends BaseQuery {
    private String createdBy;
    private String dutyDate;
    private String dateType;
    private String leaveType;
    private LocalDate dutyDateStart;
    private LocalDate dutyDateEnd;

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getDutyDateStart() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else if(dutyDateStart!=null){
            return dutyDateStart;
        }
        return null;
    }

    public LocalDate getDutyDateEnd() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else if(dutyDateEnd!=null){
            return dutyDateEnd.plusDays(1);
        }
        return null;
    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public void setDutyDateEnd(LocalDate dutyDateEnd) {
        this.dutyDateEnd = dutyDateEnd;
    }
}
