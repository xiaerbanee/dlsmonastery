package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_pricesystem_detail")
public class PricesystemDetail extends IdEntity<PricesystemDetail> {
    private BigDecimal price;
    private Integer sort;
    private Product product;
    private String productId;
    private Pricesystem pricesystem;
    private String pricesystemId;


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
}
