package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.IdEntity;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_price_change_product")
public class PriceChangeProduct extends IdEntity<PriceChangeProduct> {
    private BigDecimal amount;
    private BigDecimal price3;
    private String productId;
    private String priceChangeId;
    private String productTypeId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPriceChangeId() {
        return priceChangeId;
    }

    public void setPriceChangeId(String priceChangeId) {
        this.priceChangeId = priceChangeId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }
}
