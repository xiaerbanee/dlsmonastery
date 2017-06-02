package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class GoodsOrderBillDetailForm extends BaseForm<GoodsOrderDetail> {
    //表单信息
    private String goodsOrderDetailId;
    private String productId;
    private Integer qty;
    private Integer billQty;
    private BigDecimal price;
    //显示信息
    private String productName;
    private Boolean hasIme;
    private Boolean allowBill;
    //办事处已开单数
    private Integer areaQty;

    public String getGoodsOrderDetailId() {
        return goodsOrderDetailId;
    }

    public void setGoodsOrderDetailId(String goodsOrderDetailId) {
        this.goodsOrderDetailId = goodsOrderDetailId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public Boolean getAllowBill() {
        return allowBill;
    }

    public void setAllowBill(Boolean allowBill) {
        this.allowBill = allowBill;
    }

    public Integer getAreaQty() {
        return areaQty;
    }

    public void setAreaQty(Integer areaQty) {
        this.areaQty = areaQty;
    }
}
