package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_store_allot_ime")
public class StoreAllotIme extends CompanyEntity<StoreAllotIme> {
    private Integer version = 0;
    private ProductIme productIme;
    private String productImeId;
    private Product product;
    private String productId;
    private StoreAllot storeAllot;
    private String storeAllotId;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ProductIme getProductIme() {
        return productIme;
    }

    public void setProductIme(ProductIme productIme) {
        this.productIme = productIme;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
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

    public StoreAllot getStoreAllot() {
        return storeAllot;
    }

    public void setStoreAllot(StoreAllot storeAllot) {
        this.storeAllot = storeAllot;
    }

    public String getStoreAllotId() {
        return storeAllotId;
    }

    public void setStoreAllotId(String storeAllotId) {
        this.storeAllotId = storeAllotId;
    }
}
