package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.IdDto;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;

import java.math.BigDecimal;


public class AdGoodsOrderDetailSimpleDto extends IdDto<AdGoodsOrderDetail> {

    private String productId;
    private String productOutId;
    private String productName;
    private String productImage;
    private String productCode;
    private String productRemarks;
    private BigDecimal productPrice2;
    private Integer qty;
    private Integer confirmQty;
    private Integer billQty;
    private Integer cloudQty;
    private Integer shippedQty;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductOutId() {
        return productOutId;
    }

    public void setProductOutId(String productOutId) {
        this.productOutId = productOutId;
    }

    public Integer getWaitShipQty(){
        if(shippedQty != null && billQty != null){
            return billQty - shippedQty;
        }

        return null;
    }
    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public Integer getCloudQty() {
        return cloudQty;
    }

    public void setCloudQty(Integer cloudQty) {
        this.cloudQty = cloudQty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

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
