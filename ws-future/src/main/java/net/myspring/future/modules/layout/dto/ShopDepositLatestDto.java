package net.myspring.future.modules.layout.dto;

import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ShopDepositLatestDto {

    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;

    private String shopOfficeId;
    @CacheInput(inputKey = "offices",inputInstance = "shopOfficeId",outputInstance = "name")
    private String shopOfficeName;

    private String shopAreaId;
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;

    private BigDecimal leftMarketAmount;
    private LocalDateTime marketDepositCreatedDate;
    private String marketDepositCreatedBy;
    @CacheInput(inputKey = "accounts",inputInstance = "marketDepositCreatedBy",outputInstance = "loginName")
    private String marketDepositCreatedByName;

    private BigDecimal leftImageAmount;
    private LocalDateTime imageDepositCreatedDate;
    private String imageDepositCreatedBy;
    @CacheInput(inputKey = "accounts",inputInstance = "imageDepositCreatedBy",outputInstance = "loginName")
    private String imageDepositCreatedByName;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOfficeId() {
        return shopOfficeId;
    }

    public void setShopOfficeId(String shopOfficeId) {
        this.shopOfficeId = shopOfficeId;
    }

    public String getShopOfficeName() {
        return shopOfficeName;
    }

    public void setShopOfficeName(String shopOfficeName) {
        this.shopOfficeName = shopOfficeName;
    }

    public String getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(String shopAreaId) {
        this.shopAreaId = shopAreaId;
    }

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public BigDecimal getLeftMarketAmount() {
        return leftMarketAmount;
    }

    public void setLeftMarketAmount(BigDecimal leftMarketAmount) {
        this.leftMarketAmount = leftMarketAmount;
    }

    public LocalDateTime getMarketDepositCreatedDate() {
        return marketDepositCreatedDate;
    }

    public void setMarketDepositCreatedDate(LocalDateTime marketDepositCreatedDate) {
        this.marketDepositCreatedDate = marketDepositCreatedDate;
    }

    public String getMarketDepositCreatedBy() {
        return marketDepositCreatedBy;
    }

    public void setMarketDepositCreatedBy(String marketDepositCreatedBy) {
        this.marketDepositCreatedBy = marketDepositCreatedBy;
    }

    public String getMarketDepositCreatedByName() {
        return marketDepositCreatedByName;
    }

    public void setMarketDepositCreatedByName(String marketDepositCreatedByName) {
        this.marketDepositCreatedByName = marketDepositCreatedByName;
    }

    public BigDecimal getLeftImageAmount() {
        return leftImageAmount;
    }

    public void setLeftImageAmount(BigDecimal leftImageAmount) {
        this.leftImageAmount = leftImageAmount;
    }

    public LocalDateTime getImageDepositCreatedDate() {
        return imageDepositCreatedDate;
    }

    public void setImageDepositCreatedDate(LocalDateTime imageDepositCreatedDate) {
        this.imageDepositCreatedDate = imageDepositCreatedDate;
    }

    public String getImageDepositCreatedBy() {
        return imageDepositCreatedBy;
    }

    public void setImageDepositCreatedBy(String imageDepositCreatedBy) {
        this.imageDepositCreatedBy = imageDepositCreatedBy;
    }

    public String getImageDepositCreatedByName() {
        return imageDepositCreatedByName;
    }

    public void setImageDepositCreatedByName(String imageDepositCreatedByName) {
        this.imageDepositCreatedByName = imageDepositCreatedByName;
    }
}
