package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class GoodsOrderDetailForm extends DataForm<GoodsOrderDetail> {


    private String productName;
    private String productId;
    private Boolean productHasIme;
    private BigDecimal price;
    private Integer qty;
    private Boolean productAllowOrder;
    private Boolean productAllowBill;
    private Integer alreadyOrderQty;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getProductHasIme() {
        return productHasIme;
    }

    public void setProductHasIme(Boolean productHasIme) {
        this.productHasIme = productHasIme;
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

    public Boolean getProductAllowOrder() {
        return productAllowOrder;
    }

    public void setProductAllowOrder(Boolean productAllowOrder) {
        this.productAllowOrder = productAllowOrder;
    }

    public Boolean getProductAllowBill() {
        return productAllowBill;
    }

    public void setProductAllowBill(Boolean productAllowBill) {
        this.productAllowBill = productAllowBill;
    }

    public Integer getAlreadyOrderQty() {
        return alreadyOrderQty;
    }

    public void setAlreadyOrderQty(Integer alreadyOrderQty) {
        this.alreadyOrderQty = alreadyOrderQty;
    }
}
