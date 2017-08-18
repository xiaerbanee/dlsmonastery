package net.myspring.report.modules.future.dto;

import net.myspring.common.dto.DataDto;

import java.math.BigDecimal;

public class ProductMonthPriceDetailDto extends DataDto{
    private BigDecimal price2;
    private BigDecimal price3;
    private BigDecimal baokaPrice;
    private String productTypeId;
    private String productMonthPriceId;

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public BigDecimal getBaokaPrice() {
        return baokaPrice;
    }

    public void setBaokaPrice(BigDecimal baokaPrice) {
        this.baokaPrice = baokaPrice;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductMonthPriceId() {
        return productMonthPriceId;
    }

    public void setProductMonthPriceId(String productMonthPriceId) {
        this.productMonthPriceId = productMonthPriceId;
    }
}
