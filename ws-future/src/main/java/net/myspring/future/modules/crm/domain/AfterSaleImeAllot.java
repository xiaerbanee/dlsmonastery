package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_after_sale_ime_allot")
public class AfterSaleImeAllot extends DataEntity<AfterSaleImeAllot> {
    private String fromDepotId;
    private String toDepotId;
    private Integer version = 0;
    private String productImeId;
    private String afterSaleId;



    public AfterSaleImeAllot(String afterSaleId,String productImeId,String fromDepotId,String toDepotId,String remarks) {
        this.afterSaleId = afterSaleId;
        this.productImeId = productImeId;
        this.fromDepotId = fromDepotId;
        this.toDepotId = toDepotId;
        this.remarks = remarks;
    }

    public AfterSaleImeAllot(){};

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

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
}
