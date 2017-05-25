package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="crm_ime_allot")
public class ImeAllot extends AuditEntity<ImeAllot> {
    private String fromDepotId;
    private String toDepotId;
    private Integer version = 0;
    private Boolean crossArea;

    private String productImeId;


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

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

}
