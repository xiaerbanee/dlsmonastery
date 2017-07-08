package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdPricesystemChangeDto extends DataDto<AdPricesystemChange> {

    private String productId;
    private String productName;
    private String productCode;
    private String adPricesystemId;
    private String adPricesystemName;

    private BigDecimal oldPrice;
    private BigDecimal newPrice;

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
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

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }

    public String getAdPricesystemName() {
        return adPricesystemName;
    }

    public void setAdPricesystemName(String adPricesystemName) {
        this.adPricesystemName = adPricesystemName;
    }
}
