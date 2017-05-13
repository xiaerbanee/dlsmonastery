package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

/**
 * Created by zhangyf on 2017/5/13.
 */
public class AdGoodsOrderDetailDto extends DataDto<AdGoodsOrderDetail> {

    private BigDecimal price;
    private Integer qty;
    private Integer confirmQty;
    private Integer billQty;
    private Integer shippedQty;
    private BigDecimal shouldPay;
    private BigDecimal shouldGet;
    private String productId;
    @CacheInput(inputKey = "products", inputInstance = "productId", outputInstance = "name")
    private String productName;
    @CacheInput(inputKey = "products", inputInstance = "productId", outputInstance = "code")
    private String productCode;
    private String adGoodsOrderId;

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

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public BigDecimal getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(BigDecimal shouldPay) {
        this.shouldPay = shouldPay;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
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

    public String getAdGoodsOrderId() {
        return adGoodsOrderId;
    }

    public void setAdGoodsOrderId(String adGoodsOrderId) {
        this.adGoodsOrderId = adGoodsOrderId;
    }
}
