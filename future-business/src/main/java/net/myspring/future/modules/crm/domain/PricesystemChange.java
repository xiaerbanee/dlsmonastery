package net.myspring.future.modules.crm.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="crm_pricesystem_change")
public class PricesystemChange extends AuditEntity<PricesystemChange> {
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer version = 0;
    private Pricesystem pricesystem;
    private String pricesystemId;
    private Product product;
    private String productId;

    @Transient
    private List<List<String>> data = Lists.newArrayList();

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

    public Pricesystem getPricesystem() {
        return pricesystem;
    }

    public void setPricesystem(Pricesystem pricesystem) {
        this.pricesystem = pricesystem;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
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

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
