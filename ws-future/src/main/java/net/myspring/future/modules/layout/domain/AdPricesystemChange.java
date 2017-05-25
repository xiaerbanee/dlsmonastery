package net.myspring.future.modules.layout.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_ad_pricesystem_change")
public class AdPricesystemChange extends CompanyEntity<AdPricesystemChange> {
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer version = 0;

    private String productId;

    private String adPricesystemId;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }
}
