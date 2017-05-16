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
    private Integer billQty;
    private Integer areaQty;
    private Integer areaBillQty;
    private Boolean productAllowOrder;
    private Boolean productAllowBill;
    private String goodsOrderId;

    public Integer getAreaBillQty() {
        return areaBillQty;
    }

    public void setAreaBillQty(Integer areaBillQty) {
        this.areaBillQty = areaBillQty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
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

    public Boolean getProductAllowOrderAndBill(){

        if(productAllowBill!=null && productAllowOrder !=null){
            if(productAllowBill && productAllowOrder){
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }


    public Integer getAreaQty() {
        return areaQty;
    }

    public void setAreaQty(Integer areaQty) {
        this.areaQty = areaQty;
    }

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

}
