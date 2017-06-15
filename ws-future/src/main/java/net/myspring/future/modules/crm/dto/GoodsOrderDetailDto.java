package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

public class GoodsOrderDetailDto extends DataDto<GoodsOrder> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private Boolean hasIme;
    private BigDecimal price;
    private Integer qty;
    private Integer returnQty;
    private Integer billQty;

    private Integer areaQty;
    private Boolean allowOrder;
    private Boolean allowBill;

    //发货信息
    private Integer shippedQty;

    private Integer shipQty=0;
    private Integer leftQty;

    private Integer realBillQty;

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Boolean getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(Boolean allowOrder) {
        this.allowOrder = allowOrder;
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

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Integer getReturnQty() {
        if(returnQty==null) {
            returnQty = 0 ;
        }
        return returnQty;
    }

    public void setReturnQty(Integer returnQty) {
        this.returnQty = returnQty;
    }

    public Integer getShippedQty() {
        if(shippedQty==null) {
            shippedQty = 0 ;
        }
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public Integer getShipQty() {
        return shipQty;
    }

    public void setShipQty(Integer shipQty) {
        this.shipQty = shipQty;
    }

    public Integer getLeftQty() {
        return leftQty;
    }

    public void setLeftQty(Integer leftQty) {
        this.leftQty = leftQty;
    }

    public Integer getRealBillQty() {
        return realBillQty;
    }

    public void setRealBillQty(Integer realBillQty) {
        this.realBillQty = realBillQty;
    }
}
