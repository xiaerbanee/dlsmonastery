package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;


public class ShopDepositDto extends DataDto<ShopDeposit> {

    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;

    private String shopOfficeId;
    @CacheInput(inputKey = "offices",inputInstance = "shopOfficeId",outputInstance = "name")
    private String shopOfficeName;

    private String shopAreaId;
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;

    private String type;
    private String outCode;
    private BigDecimal amount;
    private BigDecimal leftAmount;
    private Boolean locked;
    private Boolean enabled;

    public String getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(String shopAreaId) {
        this.shopAreaId = shopAreaId;
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

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public String getShopOfficeName() {
        return shopOfficeName;
    }

    public void setShopOfficeName(String shopOfficeName) {
        this.shopOfficeName = shopOfficeName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(BigDecimal leftAmount) {
        this.leftAmount = leftAmount;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }



}
