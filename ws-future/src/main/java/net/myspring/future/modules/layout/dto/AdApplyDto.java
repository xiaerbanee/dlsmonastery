package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;


/**
 * Created by lihx on 2017/4/15.
 */
public class AdApplyDto extends DataDto<AdApply> {

    private String shopId;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "name")
    private String shopName;
    private Integer applyQty;
    private Integer confirmQty;
    private Integer billedQty;
    private String orderId;
    private Integer leftQty;
    private String expiryDateRemarks;
    private Integer version;
    private String productId;
    @CacheInput(inputKey = "products", inputInstance = "productId", outputInstance = "name")
    private String productName;
    @CacheInput(inputKey = "products", inputInstance = "productId", outputInstance = "code")
    private String productCode;
    @CacheInput(inputKey = "products", inputInstance = "productId", outputInstance = "price2")
    private BigDecimal price2;
    @CacheInput(inputKey = "products", inputInstance = "productId", outputInstance = "outId")
    private String productOutId;
    private Integer storeQty;
    private Integer nowBilledQty;

    public String getProductOutId() {
        return productOutId;
    }

    public void setProductOutId(String productOutId) {
        this.productOutId = productOutId;
    }

    public Integer getNowBilledQty() {
        return nowBilledQty;
    }

    public void setNowBilledQty(Integer nowBilledQty) {
        this.nowBilledQty = nowBilledQty;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(Integer applyQty) {
        this.applyQty = applyQty;
    }

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public Integer getBilledQty() {
        return billedQty;
    }

    public void setBilledQty(Integer billedQty) {
        this.billedQty = billedQty;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getLeftQty() {
        return leftQty;
    }

    public void setLeftQty(Integer leftQty) {
        this.leftQty = leftQty;
    }

    public String getExpiryDateRemarks() {
        return expiryDateRemarks;
    }

    public void setExpiryDateRemarks(String expiryDateRemarks) {
        this.expiryDateRemarks = expiryDateRemarks;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getStoreQty() {
        return storeQty;
    }

    public void setStoreQty(Integer storeQty) {
        this.storeQty = storeQty;
    }

}
