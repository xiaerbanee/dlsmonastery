package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

public class GoodsOrderDetailDto extends DataDto<GoodsOrder> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private Boolean productHasIme;
    private BigDecimal price;
    private Integer qty;
    private Integer returnQty;
    private Integer billQty;

    private Integer areaQty;
    private Boolean productAllowOrder;
    private Boolean productAllowBill;

    //发货信息
    private Integer shippedQty;

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
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

    public Integer getAreaQty() {
        return areaQty;
    }

    public void setAreaQty(Integer areaQty) {
        this.areaQty = areaQty;
    }

    public Boolean getProductHasIme() {
        return productHasIme;
    }

    public void setProductHasIme(Boolean productHasIme) {
        this.productHasIme = productHasIme;
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
}
