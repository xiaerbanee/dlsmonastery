package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GoodsOrderDto extends DataDto<GoodsOrder> {


    private String shipRemarks;
    private BigDecimal shopGoodsDeposit;
    private Boolean isUseTicket;
    private String carrierShopName;
    private String outCode;
    private String shopAreaName;
    private String businessId;
    private LocalDateTime billDate;
    private LocalDateTime shipDate;
    private String status;
    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String storeId;
    @CacheInput(inputKey = "depots",inputInstance = "storeId",outputInstance = "name")
    private String storeName;
    private String shipType;
    private BigDecimal amount;
    private BigDecimal shopShouldGet;
    private String netType;
    private String expressOrderExpressCodes;
    private String carrierCodes;

    private BigDecimal totalShopGoodsDepositAmount;

    public BigDecimal getTotalShopGoodsDepositAmount() {
        return totalShopGoodsDepositAmount;
    }

    public void setTotalShopGoodsDepositAmount(BigDecimal totalShopGoodsDepositAmount) {
        this.totalShopGoodsDepositAmount = totalShopGoodsDepositAmount;
    }


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShipRemarks() {
        return shipRemarks;
    }

    public void setShipRemarks(String shipRemarks) {
        this.shipRemarks = shipRemarks;
    }

    public BigDecimal getShopGoodsDeposit() {
        return shopGoodsDeposit;
    }

    public void setShopGoodsDeposit(BigDecimal shopGoodsDeposit) {
        this.shopGoodsDeposit = shopGoodsDeposit;
    }

    public Boolean getUseTicket() {
        return isUseTicket;
    }

    public void setUseTicket(Boolean useTicket) {
        isUseTicket = useTicket;
    }

    public String getCarrierShopName() {
        return carrierShopName;
    }

    public void setCarrierShopName(String carrierShopName) {
        this.carrierShopName = carrierShopName;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }

    private String pullStatus;

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getShopShouldGet() {
        return shopShouldGet;
    }

    public void setShopShouldGet(BigDecimal shopShouldGet) {
        this.shopShouldGet = shopShouldGet;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getExpressOrderExpressCodes() {
        return expressOrderExpressCodes;
    }

    public void setExpressOrderExpressCodes(String expressOrderExpressCodes) {
        this.expressOrderExpressCodes = expressOrderExpressCodes;
    }

    public String getCarrierCodes() {
        return carrierCodes;
    }

    public void setCarrierCodes(String carrierCodes) {
        this.carrierCodes = carrierCodes;
    }

    public String getPullStatus() {
        return pullStatus;
    }

    public void setPullStatus(String pullStatus) {
        this.pullStatus = pullStatus;
    }
}
