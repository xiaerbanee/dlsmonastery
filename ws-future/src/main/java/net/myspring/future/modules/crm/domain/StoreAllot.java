package net.myspring.future.modules.crm.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Depot;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="crm_store_allot")
public class StoreAllot extends CompanyEntity<StoreAllot> {
    private String fromStoreId;
    private String toStoreId;
    private String outCode;
    private String status;
    private String businessId;
    private LocalDate billDate;
    private LocalDateTime shipDate;
    private Integer version = 0;
    private String shipType;
    private ExpressOrder expressOrder;
    private String expressOrderId;
    private String cloudSynId;

    private Depot toStore;
    private Depot fromStore;

    public String getFromStoreId() {
        return fromStoreId;
    }

    public void setFromStoreId(String fromStoreId) {
        this.fromStoreId = fromStoreId;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public ExpressOrder getExpressOrder() {
        return expressOrder;
    }

    public void setExpressOrder(ExpressOrder expressOrder) {
        this.expressOrder = expressOrder;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getCloudSynId() {
        return cloudSynId;
    }

    public void setCloudSynId(String cloudSynId) {
        this.cloudSynId = cloudSynId;
    }

    public Depot getToStore() {
        return toStore;
    }

    public void setToStore(Depot toStore) {
        this.toStore = toStore;
    }

    public Depot getFromStore() {
        return fromStore;
    }

    public void setFromStore(Depot fromStore) {
        this.fromStore = fromStore;
    }
}
