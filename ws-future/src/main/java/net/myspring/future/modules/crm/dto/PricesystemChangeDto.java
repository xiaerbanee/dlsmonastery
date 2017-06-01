package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/13.
 */
public class PricesystemChangeDto extends DataDto<PricesystemChange> {
    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String pricesystemId;
    @CacheInput(inputKey = "pricesystems",inputInstance = "pricesystemId",outputInstance = "name")
    private String pricesystemName;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String status;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPricesystemName() {
        return pricesystemName;
    }

    public void setPricesystemName(String pricesystemName) {
        this.pricesystemName = pricesystemName;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
