package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

public class GoodsOrderQuery extends BaseQuery {


    private String netType;
    private List<String> netTypeList;
    private String businessId;
    private String businessIds;



    private String billDateRange;
    private LocalDateTime billDateEnd;
    private LocalDateTime billDateStart;

    private String shipType;
    private List<String> shipTypeList;
    private String areaId;
    private List<String> areaList;

    private String shipDateRange;
    private LocalDateTime shipDateEnd;
    private LocalDateTime shipDateStart;

    private String shopName;
    private String storeId;
    private String createdBy;

    private String outCode;

    private String createdDateRange;
    private LocalDateTime createdDateEnd;
    private LocalDateTime createdDateStart;

    private String expressCodes;
    private String expressCode;
    private String status;
    private List<String> statusList;
    private String remarks;

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public List<String> getNetTypeList() {
        return netTypeList;
    }

    public void setNetTypeList(List<String> netTypeList) {
        this.netTypeList = netTypeList;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(String businessIds) {
        this.businessIds = businessIds;
    }

    public String getBillDateRange() {
        return billDateRange;
    }

    public void setBillDateRange(String billDateRange) {

        if(billDateRange!=null){
            String[] tempParamValues = billDateRange.split(" - ");
            this.billDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.billDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.billDateRange = billDateRange;

    }

    public LocalDateTime getBillDateEnd() {
        return billDateEnd;
    }

    public LocalDateTime getBillDateStart() {
        return billDateStart;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public List<String> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<String> areaList) {
        this.areaList = areaList;
    }

    public String getShipDateRange() {
        return shipDateRange;
    }

    public void setShipDateRange(String shipDateRange) {
        if(shipDateRange!=null){
            String[] tempParamValues = shipDateRange.split(" - ");
            this.shipDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.shipDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.shipDateRange = shipDateRange;

    }

    public LocalDateTime getShipDateEnd() {
        return shipDateEnd;
    }

    public LocalDateTime getShipDateStart() {
        return shipDateStart;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getExpressCodes() {
        return expressCodes;
    }

    public void setExpressCodes(String expressCodes) {
        this.expressCodes = expressCodes;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
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



    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.createdDateRange = createdDateRange;

    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }


    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }



}
