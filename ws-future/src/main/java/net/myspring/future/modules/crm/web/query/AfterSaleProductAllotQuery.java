package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhucc on 2017/7/4.
 */
public class AfterSaleProductAllotQuery extends BaseQuery{

    private String createdDate;
    private String businessId;
    private List<String> toOutCode;
    private String fromProductName;
    private String storeName;
    private List<String> fromOutCode;
    private String toProductName;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


    public String getBusinessId() {
        if(StringUtils.isNotBlank(businessId)){
            this.businessId=businessId.replace("XK","");
        }
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFromProductName() {
        return fromProductName;
    }

    public void setFromProductName(String fromProductName) {
        this.fromProductName = fromProductName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getToProductName() {
        return toProductName;
    }

    public void setToProductName(String toProductName) {
        this.toProductName = toProductName;
    }

    public List<String> getToOutCode() {
        return toOutCode;
    }

    public void setToOutCode(List<String> toOutCode) {
        this.toOutCode = toOutCode;
    }

    public List<String> getFromOutCode() {
        return fromOutCode;
    }

    public void setFromOutCode(List<String> fromOutCode) {
        this.fromOutCode = fromOutCode;
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        }
        return null;
    }


    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }
        return null;
    }

}
