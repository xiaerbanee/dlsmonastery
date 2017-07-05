package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GoodsOrderImeQuery extends BaseQuery {

    private String businessId;
    private String imes;
    private Boolean lxMallOrder;
    private String billDateRange;
    private String shipDateRange;
    private String createdDateRange;
    private String shopName;
    private String storeName;
    private String productName;
    private String createdBy;
    private String status;
    private String remarks;

    public List<String> getImeList() {
        if(StringUtils.isNotBlank(imes)) {
            return StringUtils.getSplitList(imes, CharConstant.ENTER);
        } else {
            return null;
        }
    }

    public LocalDate getBillDateStart() {
        if(StringUtils.isNotBlank(billDateRange)) {
            return LocalDateUtils.parse(billDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getBillDateEnd() {
        if(StringUtils.isNotBlank(billDateRange)) {
            return LocalDateUtils.parse(billDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getShipDateStart() {
        if(StringUtils.isNotBlank(shipDateRange)) {
            return LocalDateUtils.parse(shipDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getShipDateEnd() {
        if(StringUtils.isNotBlank(shipDateRange)) {
            return LocalDateUtils.parse(shipDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDateRange)) {
            return LocalDateUtils.parse(createdDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDateRange)) {
            return LocalDateUtils.parse(createdDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getImes() {
        return imes;
    }

    public void setImes(String imes) {
        this.imes = imes;
    }

    public Boolean getLxMallOrder() {
        return lxMallOrder;
    }

    public void setLxMallOrder(Boolean lxMallOrder) {
        this.lxMallOrder = lxMallOrder;
    }

    public String getBillDateRange() {
        return billDateRange;
    }

    public void setBillDateRange(String billDateRange) {
        this.billDateRange = billDateRange;
    }

    public String getShipDateRange() {
        return shipDateRange;
    }

    public void setShipDateRange(String shipDateRange) {
        this.shipDateRange = shipDateRange;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        this.createdDateRange = createdDateRange;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
