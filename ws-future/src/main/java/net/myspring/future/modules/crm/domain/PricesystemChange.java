package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_pricesystem_change")
public class PricesystemChange extends AuditEntity<PricesystemChange> {
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer version = 0;

    private String pricesystemId;

    private String productId;



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

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
