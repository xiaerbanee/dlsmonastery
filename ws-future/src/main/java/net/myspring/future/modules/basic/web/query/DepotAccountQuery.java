package net.myspring.future.modules.basic.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

public class DepotAccountQuery extends BaseQuery {

    private String name;
    private String dutyDateRange;
    private String officeId;
    private String areaId;
    private Boolean specialityStore;
    private Boolean accountTaxPermitted;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Boolean getAccountTaxPermitted() {
        return accountTaxPermitted;
    }

    public void setAccountTaxPermitted(Boolean accountTaxPermitted) {
        this.accountTaxPermitted = accountTaxPermitted;
    }

    public LocalDate getDutyDateStart() {
        if(StringUtils.isNotBlank(dutyDateRange)) {
            return LocalDateUtils.parse(dutyDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getDutyDateEnd() {
        if(StringUtils.isNotBlank(dutyDateRange)) {
            return LocalDateUtils.parse(dutyDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDutyDateRange() {
        return dutyDateRange;
    }

    public void setDutyDateRange(String dutyDateRange) {
        this.dutyDateRange = dutyDateRange;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Boolean getSpecialityStore() {
        return specialityStore;
    }

    public void setSpecialityStore(Boolean specialityStore) {
        this.specialityStore = specialityStore;
    }
}
