package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StoreAllotQuery extends BaseQuery {

    private String fromStoreId;
    private String toStoreId;
    private String status;
    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private String outCode;
    private String remarks;
    private String createdBy;
    private String businessIds;
    private String shipType;
    private List<String> statusList = new ArrayList<String>();
    private List<String> shipTypeList = new ArrayList<String>();

    public List<String> getBusinessIdList() {
        if(StringUtils.isNotBlank(businessIds)) {
            return StringUtils.getSplitList(businessIds, CharConstant.ENTER);
        } else {
            return null;
        }
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(String businessIds) {
        this.businessIds = businessIds;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFromStoreId() {
        return fromStoreId;
    }

    public void setFromStoreId(String fromStoreId) {
        this.fromStoreId = fromStoreId;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.createdDateRange = createdDateRange;

    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }
}
