package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_depot_detail")
public class DepotDetail extends CompanyEntity<DepotDetail> {
    private Integer qty;
    private Boolean hasIme;
    private Integer version = 0;
    private Boolean isSame;
    private Integer outQty;
    private Product product;
    private String productId;
    private Depot depot;
    private String depotId;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsSame() {
        return isSame;
    }

    public void setIsSame(Boolean isSame) {
        this.isSame = isSame;
    }

    public Integer getOutQty() {
        return outQty;
    }

    public void setOutQty(Integer outQty) {
        this.outQty = outQty;
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

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }
}
