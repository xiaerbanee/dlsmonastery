package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleQuery {

    private String id;
    private String remarks;
    private String createdDate;
    private String toCompanyDate;
    private String toStoreDate;
    private String fromCompanyDate;

    public String getFromCompanyDate() {
        return fromCompanyDate;
    }

    public void setFromCompanyDate(String fromCompanyDate) {
        this.fromCompanyDate = fromCompanyDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getToCompanyDate() {
        return toCompanyDate;
    }

    public void setToCompanyDate(String toCompanyDate) {
        this.toCompanyDate = toCompanyDate;
    }

    public String getToStoreDate() {
        return toStoreDate;
    }

    public void setToStoreDate(String toStoreDate) {
        this.toStoreDate = toStoreDate;
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getToCompanyDateStart() {
        if(StringUtils.isNotBlank(toCompanyDate)) {
            return LocalDateUtils.parse(toCompanyDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getToCompanyDateEnd() {
        if(StringUtils.isNotBlank(toCompanyDate)) {
            return LocalDateUtils.parse(toCompanyDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getToStoreDateStart() {
        if(StringUtils.isNotBlank(toStoreDate)) {
            return LocalDateUtils.parse(toStoreDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getToStoreDateEnd() {
        if(StringUtils.isNotBlank(toStoreDate)) {
            return LocalDateUtils.parse(toStoreDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}
