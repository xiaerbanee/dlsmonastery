package net.myspring.future.modules.layout.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AdGoodsOrderQuery extends BaseQuery {
    private String idStr;
    private List<String> shopAreaId = new ArrayList<>();
    private String storeId;
    private String shopName;
    private String billType = BillTypeEnum.柜台.name();
    private String parentId;
    private List<String> processStatus=new ArrayList<>();
    private String remarks;
    private String createdBy;
    private String createdDateRange;
    private String billDateRange;
    private Boolean hasDeposit;

    public List<String> getIdList(){
        if(idStr == null){
            return new ArrayList<>();
        }

        return Arrays.asList(idStr.split(CharConstant.COMMA+CharConstant.VERTICAL_LINE+CharConstant.ENTER+CharConstant.VERTICAL_LINE+CharConstant.SPACE));
    }

    public Boolean getHasDeposit() {
        return hasDeposit;
    }

    public void setHasDeposit(Boolean hasDeposit) {
        this.hasDeposit = hasDeposit;
    }

    public List<String> getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(List<String> shopAreaId) {
        this.shopAreaId = shopAreaId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public List<String> getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(List<String> processStatus) {
        this.processStatus = processStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        this.createdDateRange = createdDateRange;
    }

    public String getBillDateRange() {
        return billDateRange;
    }

    public void setBillDateRange(String billDateRange) {
        this.billDateRange = billDateRange;
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
}
