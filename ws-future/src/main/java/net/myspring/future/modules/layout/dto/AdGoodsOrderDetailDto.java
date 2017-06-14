package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;

import java.math.BigDecimal;


public class AdGoodsOrderDetailDto extends DataDto<AdGoodsOrderDetail> {

    private String productId;
    private String productName;
    private String productCode;
    private String productRemarks;
    private BigDecimal productPrice2;
    private Integer qty;
    private Integer confirmQty;

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductRemarks() {
        return productRemarks;
    }

    public void setProductRemarks(String productRemarks) {
        this.productRemarks = productRemarks;
    }

    public BigDecimal getProductPrice2() {
        return productPrice2;
    }

    public void setProductPrice2(BigDecimal productPrice2) {
        this.productPrice2 = productPrice2;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
