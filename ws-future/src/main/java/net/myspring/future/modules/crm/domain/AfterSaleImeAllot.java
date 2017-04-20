package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Depot;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_after_sale_ime_allot")
public class AfterSaleImeAllot extends CompanyEntity<AfterSaleImeAllot> {
    private String fromDepotId;
    private String toDepotId;
    private Integer version = 0;
    private ProductIme productIme;
    private String productImeId;
    private AfterSale afterSale;
    private String afterSaleId;

    @Transient
    private Depot fromDepot;
    @Transient
    private Depot toDepot;


    public AfterSaleImeAllot(String afterSaleId,String productImeId,String fromDepotId,String toDepotId,String remarks) {
        this.afterSaleId = afterSaleId;
        this.productImeId = productImeId;
        this.fromDepotId = fromDepotId;
        this.toDepotId = toDepotId;
        this.remarks = remarks;
    }

    public AfterSaleImeAllot(){};

    public void setFromDepot(Depot fromDepot) {
        this.fromDepot = fromDepot;
    }

    public Depot getFromDepot() {
        return fromDepot;
    }

    public void setToDepot(Depot toDepot) {
        this.toDepot = toDepot;
    }

    public Depot getToDepot() {
        return toDepot;
    }

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

    public AfterSale getAfterSale() {
        return afterSale;
    }

    public void setAfterSale(AfterSale afterSale) {
        this.afterSale = afterSale;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
}
