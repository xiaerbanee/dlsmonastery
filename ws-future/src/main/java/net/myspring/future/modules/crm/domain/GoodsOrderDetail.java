package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;


@Entity
@Table(name="crm_goods_order_detail")
public class GoodsOrderDetail extends IdEntity<GoodsOrderDetail> {
    private BigDecimal price;
    private Integer qty;
    private Integer billQty;
    private Integer shippedQty;
    private Integer returnQty;
    private Product product;
    private String productId;
    private GoodsOrder goodsOrder;
    private String goodsOrderId;
    @Transient
    private Integer shipQty;
    @Transient
    private Integer areaQty;
    @Transient
    private Integer storeQty;

    public Integer getStoreQty() {
        return storeQty;
    }

    public void setStoreQty(Integer storeQty) {
        this.storeQty = storeQty;
    }

    public Integer getAreaQty() {
        return areaQty;
    }

    public void setAreaQty(Integer areaQty) {
        this.areaQty = areaQty;
    }

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

    public Integer getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(Integer returnQty) {
        this.returnQty = returnQty;
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

    public GoodsOrder getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(GoodsOrder goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }


    public Integer getRealBillQty() {
        if (getReturnQty() == null || getReturnQty() == 0) {
            return getBillQty();
        } else {
            return getBillQty() - getReturnQty();
        }
    }
}
