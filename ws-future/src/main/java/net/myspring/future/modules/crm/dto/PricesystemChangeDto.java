package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/13.
 */
public class PricesystemChangeDto extends DataDto<PricesystemChange> {
    private String productName;
    private String pricesystemName;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private LocalDateTime createdDate;
    private String status;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
