package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_ad_apply")
public class AdApply extends DataEntity<AdApply> {
    private String shopId;
    private Integer applyQty;
    private Integer confirmQty;
    private Integer billedQty;
    private String orderId;
    private Integer leftQty;
    private String expiryDateRemarks;
    private Integer version=0;
    private Product product;
    private String productId;

    private Depot shop;
    @Transient
    private Integer storeQty;
    @Transient
    private Integer billQty;

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }

    public Integer getStoreQty() {
        return storeQty;
    }

    public void setStoreQty(Integer storeQty) {
        this.storeQty = storeQty;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
