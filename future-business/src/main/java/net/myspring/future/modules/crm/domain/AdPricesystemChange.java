package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_ad_pricesystem_change")
public class AdPricesystemChange extends DataEntity<AdPricesystemChange> {
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer version = 0;
    private Product product;
    private String productId;
    private AdPricesystem adPricesystem;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public AdPricesystem getAdPricesystem() {
        return adPricesystem;
    }

    public void setAdPricesystem(AdPricesystem adPricesystem) {
        this.adPricesystem = adPricesystem;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }
}
