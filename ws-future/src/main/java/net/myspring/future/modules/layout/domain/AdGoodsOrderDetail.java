package net.myspring.future.modules.layout.domain;


import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Table(name="crm_ad_goods_order_detail")
public class AdGoodsOrderDetail extends IdEntity<AdGoodsOrderDetail> {
    private BigDecimal price;
    private Integer qty;
    private Integer confirmQty;
    private Integer billQty;
    private Integer shippedQty;
    private BigDecimal shouldPay;
    private BigDecimal shouldGet;

    private String productId;

    private String adGoodsOrderId;

    @Transient
    private Integer shipQty;

    public Integer getShipQty() {
        return shipQty;
    }

    public void setShipQty(Integer shipQty) {
        this.shipQty = shipQty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public BigDecimal getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(BigDecimal shouldPay) {
        this.shouldPay = shouldPay;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAdGoodsOrderId() {
        return adGoodsOrderId;
    }

    public void setAdGoodsOrderId(String adGoodsOrderId) {
        this.adGoodsOrderId = adGoodsOrderId;
    }
}
