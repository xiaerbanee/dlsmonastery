package net.myspring.basic.modules.hr.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.enums.DutyRestTypeEnum;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutyRestQuery {
    private String createdBy;
    private String dutyDate;
    private String type;
    private String dateType;
    private List<String> officeIds;
    private List<String> restList= Lists.newArrayList();
    private List<String> dateList= Lists.newArrayList();

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

    public void setRestList(List<String> restList) {
        this.restList = restList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<String> getRestList() {
        return restList;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public LocalDate getDutyDateStart() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getDutyDateEnd() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}
