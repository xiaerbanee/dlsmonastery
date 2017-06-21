package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

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
    private String requestClient;
    private LocalDate dutyDateStart;
    private LocalDate dutyDateEnd;

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public String getRequestClient() {
        return requestClient;
    }

    public void setRequestClient(String requestClient) {
        this.requestClient = requestClient;
    }

    public String getCreatedBy() {
        if(StringUtils.isNotEmpty(requestClient)&&"weixin".equals(requestClient)){
            this.createdBy= RequestUtils.getAccountId();
        }
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
        } else  if(dutyDateStart!=null){
            return dutyDateStart;
        }
            return null;

    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.dutyDateStart = dutyDateStart;
    }

    public LocalDate getDutyDateEnd() {
        if(StringUtils.isNotBlank(dutyDate)) {
            return LocalDateUtils.parse(dutyDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else if(dutyDateEnd!=null){
            return dutyDateEnd.plusDays(1);
        }
            return null;
        }

    public void setDutyDateEnd(LocalDate dutyDateEnd) {
        this.dutyDateEnd = dutyDateEnd;
    }
}
