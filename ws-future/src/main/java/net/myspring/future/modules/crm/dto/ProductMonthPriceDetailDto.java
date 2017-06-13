package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;


public class ProductMonthPriceDetailDto extends DataDto<ProductMonthPriceDetail> {

    private String productTypeId;
    @CacheInput(inputKey = "productTypes",inputInstance = "productTypeId",outputInstance = "name")
    private String productTypeName;
    private BigDecimal price2;
    private BigDecimal price3;
    private BigDecimal baokaPrice;

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

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

}
