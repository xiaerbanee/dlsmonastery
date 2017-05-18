package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class GoodsOrderDetailForm extends DataForm<GoodsOrderDetail> {
    private String goodsOrderId;
    private String prodcutId;
    private Integer qty;
    private Integer billQty;
    private BigDecimal price;
    private Boolean hasIme;


    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getProdcutId() {
        return prodcutId;
    }

    public void setProdcutId(String prodcutId) {
        this.prodcutId = prodcutId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }
}
