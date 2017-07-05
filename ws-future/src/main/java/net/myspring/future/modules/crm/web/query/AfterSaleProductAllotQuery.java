package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.collection.CollectionUtil;
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
    private String toOutCode;
    private String fromOutCode;
    private String fromProductName;
    private String storeName;
    private List<String> toOutCodeList;
    private List<String> fromOutCodeList;
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

    public String getToOutCode() {
        return toOutCode;
    }

    public void setToOutCode(String toOutCode) {
        this.toOutCode = toOutCode;
    }

    public String getFromOutCode() {
        return fromOutCode;
    }

    public void setFromOutCode(String fromOutCode) {
        this.fromOutCode = fromOutCode;
    }

    public List<String> getToOutCodeList() {
        if(CollectionUtil.isEmpty(toOutCodeList)&&StringUtils.isNotBlank(toOutCode)){
            this.toOutCodeList=StringUtils.getFilterList(toOutCode);
        }
        return toOutCodeList;
    }

    public void setToOutCodeList(List<String> toOutCodeList) {
        this.toOutCodeList = toOutCodeList;
    }

    public List<String> getFromOutCodeList() {
        if(CollectionUtil.isEmpty(fromOutCodeList)&&StringUtils.isNotBlank(fromOutCode)){
            this.fromOutCodeList=StringUtils.getFilterList(fromOutCode);
        }
        return fromOutCodeList;
    }

    public void setFromOutCodeList(List<String> fromOutCodeList) {
        this.fromOutCodeList = fromOutCodeList;
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
