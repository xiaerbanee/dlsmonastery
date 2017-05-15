package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;

import java.math.BigDecimal;

public class GoodsOrderDetailDto extends DataDto<GoodsOrder> {


    private String productName;
    private String productId;
    private Boolean productHasIme;
    private BigDecimal price;
    private Integer qty;

    private Integer areaQty;
    private Boolean productAllowOrder;
    private Boolean productAllowBill;

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
