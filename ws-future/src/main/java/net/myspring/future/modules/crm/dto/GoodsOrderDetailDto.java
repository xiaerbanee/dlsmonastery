package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

public class GoodsOrderDetailDto extends DataDto<GoodsOrder> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "outId")
    private String productOutId;

    private Boolean hasIme;
    private BigDecimal price;
    private Integer qty;
    private Integer returnQty;
    private Integer billQty;

    private Integer areaQty;
    private Boolean allowOrder;
    private Boolean visible;

    //发货信息
    private Integer shippedQty;

    private Integer shipQty=0;
    private Integer leftQty;

    private Integer realBillQty;

    private Integer storeQty;

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getProductOutId() {
        return productOutId;
    }

    public void setProductOutId(String productOutId) {
        this.productOutId = productOutId;
    }

    public Integer getStoreQty() {
        return storeQty;
    }

    public void setStoreQty(Integer storeQty) {
        this.storeQty = storeQty;
    }

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
        leftQty = getRealBillQty() - (getShippedQty()==null?0:getShippedQty())- (getShipQty()==null?0:getShipQty());
        return leftQty;
    }

    public void setLeftQty(Integer leftQty) {
        this.leftQty = leftQty;
    }

    public Integer getRealBillQty() {
        realBillQty = (getBillQty()==null?0:getBillQty()) - (getReturnQty()==null?0:getReturnQty());
        return realBillQty;
    }

    public void setRealBillQty(Integer realBillQty) {
        this.realBillQty = realBillQty;
    }
}
