package net.myspring.future.modules.crm.domain;

import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_ime_allot")
public class ImeAllot extends AuditEntity<ImeAllot> {
    private String fromDepotId;
    private String toDepotId;
    private Integer version = 0;
    private Boolean crossArea;
    private ProductIme productIme;
    private String productImeId;
    private Depot fromDepot;
    private Depot toDepot;
    @Transient
    private String imeStr;

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getCrossArea() {
        return crossArea;
    }

    public void setCrossArea(Boolean crossArea) {
        this.crossArea = crossArea;
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

    public Depot getFromDepot() {
        return fromDepot;
    }

    public void setFromDepot(Depot fromDepot) {
        this.fromDepot = fromDepot;
    }

    public Depot getToDepot() {
        return toDepot;
    }

    public void setToDepot(Depot toDepot) {
        this.toDepot = toDepot;
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }
}
