package net.myspring.basic.modules.hr.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.query.BaseQuery;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.joda.time.LocalDateTime;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class DutySignQuery extends BaseQuery {
    private String dutyDate;
    private String createdBy;
    private String address;
    private String employeeName;
    private String officeName;
    private String positionName;
    private List<String> officeIds;

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
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
