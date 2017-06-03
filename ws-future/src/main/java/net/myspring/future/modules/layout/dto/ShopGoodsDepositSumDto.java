package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShopGoodsDepositSumDto extends DataDto<ShopGoodsDeposit> {

    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String shopAreaId;
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;
    private BigDecimal totalAmount;

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
