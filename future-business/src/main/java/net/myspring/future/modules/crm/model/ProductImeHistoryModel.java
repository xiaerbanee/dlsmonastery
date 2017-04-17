package net.myspring.future.modules.crm.model;


import net.myspring.future.modules.basic.domain.Depot;

import java.time.LocalDateTime;

public class ProductImeHistoryModel {
    private String id;
    private String historyType;
    private Depot fromDepot;
    private String fromDepotId;
    private Depot toDepot;
    private String toDepotId;
    private String createdBy;
    private LocalDateTime createdDate;
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public Depot getFromDepot() {
        return fromDepot;
    }

    public void setFromDepot(Depot fromDepot) {
        this.fromDepot = fromDepot;
    }

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
    }

    public Depot getToDepot() {
        return toDepot;
    }

    public void setToDepot(Depot toDepot) {
        this.toDepot = toDepot;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
