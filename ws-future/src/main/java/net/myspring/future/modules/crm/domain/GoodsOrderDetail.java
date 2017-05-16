package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name="crm_goods_order_detail")
public class GoodsOrderDetail extends IdEntity<GoodsOrderDetail> {
    private BigDecimal price;
    private Integer qty;
    private Integer billQty;
    private Integer shippedQty;
    private Integer returnQty;
    private String productId;
    private String goodsOrderId;

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


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
