package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class GoodsOrderDetailForm extends BaseForm<GoodsOrderDetail> {
    //表单信息
    private String goodsOrderDetailId;
    private String productId;
    private Integer qty;
    private Integer returnQty;

    //显示信息
    private String productName;
    private Boolean hasIme;
    private BigDecimal price;

    private Boolean allowOrder;
    //办事处已下单数
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

    public Integer getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(Integer returnQty) {
        this.returnQty = returnQty;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(Boolean allowOrder) {
        this.allowOrder = allowOrder;
    }

    public Integer getAreaQty() {
        return areaQty;
    }

    public void setAreaQty(Integer areaQty) {
        this.areaQty = areaQty;
    }
}
