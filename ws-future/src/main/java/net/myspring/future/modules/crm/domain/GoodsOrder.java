package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="crm_goods_order")
public class GoodsOrder extends CompanyEntity<GoodsOrder> {
    private String storeId;
    private String shopId;
    private String status;
    private String pullStatus;
    private String businessId;
    private String outCode;
    private LocalDate billDate;
    private Boolean isUseTicket;
    private LocalDateTime shipDate;
    private BigDecimal amount;
    private Integer version = 0;
    private String shipType;
    private Boolean splitBill;
    private String netType;


    private String expressOrderId;


    //金蝶中间表
    private String transCloudSynId;
    private String outCloudSynId;
    private String otherCloudSynId;
    private String returnCloudSynId;
    private String retransCloudSynId;



    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPullStatus() {
        return pullStatus;
    }

    public void setPullStatus(String pullStatus) {
        this.pullStatus = pullStatus;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public Boolean getUseTicket() {
        return isUseTicket;
    }

    public void setUseTicket(Boolean useTicket) {
        isUseTicket = useTicket;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Boolean getSplitBill() {
        return splitBill;
    }

    public void setSplitBill(Boolean splitBill) {
        this.splitBill = splitBill;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getTransCloudSynId() {
        return transCloudSynId;
    }

    public void setTransCloudSynId(String transCloudSynId) {
        this.transCloudSynId = transCloudSynId;
    }

    public String getOutCloudSynId() {
        return outCloudSynId;
    }

    public void setOutCloudSynId(String outCloudSynId) {
        this.outCloudSynId = outCloudSynId;
    }

    public String getOtherCloudSynId() {
        return otherCloudSynId;
    }

    public void setOtherCloudSynId(String otherCloudSynId) {
        this.otherCloudSynId = otherCloudSynId;
    }

    public String getReturnCloudSynId() {
        return returnCloudSynId;
    }

    public void setReturnCloudSynId(String returnCloudSynId) {
        this.returnCloudSynId = returnCloudSynId;
    }

    public String getRetransCloudSynId() {
        return retransCloudSynId;
    }

    public void setRetransCloudSynId(String retransCloudSynId) {
        this.retransCloudSynId = retransCloudSynId;
    }

}
