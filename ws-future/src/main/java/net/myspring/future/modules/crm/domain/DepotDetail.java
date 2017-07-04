package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_depot_detail")
public class DepotDetail extends DataEntity<DepotDetail> {
    private Integer qty;
    private Boolean hasIme;
    private Integer version = 0;
    private Boolean isSame;
    private Integer outQty;

    private String productId;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }
}
