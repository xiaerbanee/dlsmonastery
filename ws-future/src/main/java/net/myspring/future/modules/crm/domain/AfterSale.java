package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="crm_after_sale")
public class AfterSale extends DataEntity<AfterSale> {
    private String businessId;
    private String badProductImeId;
    private String badType;
    private String replaceProductImeId;
    private String toAreaProductImeId;
    private String packageStatus;
    private String memory;
    private String toStoreType;
    private String toStoreRemarks;
    private LocalDate toStoreDate;
    private LocalDate toCompanyDate;
    private String toCompanyRemarks;
    private LocalDate fromCompanyDate;
    private String fromCompanyProductId;
    private String areaDepotId;
    private Boolean fromAreaToFinance=false;
    private Boolean toAreaToFinance=false;
    private Boolean fromCompanyToFinance=false;
    private Boolean toCompanyToFinance=false;
    private String badDepotId;
    private LocalDateTime toFinanceDate;
    private Integer version = 0;
    private String depotId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBadProductImeId() {
        return badProductImeId;
    }

    public void setBadProductImeId(String badProductImeId) {
        this.badProductImeId = badProductImeId;
    }

    public String getBadType() {
        return badType;
    }

    public void setBadType(String badType) {
        this.badType = badType;
    }

    public String getReplaceProductImeId() {
        return replaceProductImeId;
    }

    public void setReplaceProductImeId(String replaceProductImeId) {
        this.replaceProductImeId = replaceProductImeId;
    }

    public String getToAreaProductImeId() {
        return toAreaProductImeId;
    }

    public void setToAreaProductImeId(String toAreaProductImeId) {
        this.toAreaProductImeId = toAreaProductImeId;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getToStoreType() {
        return toStoreType;
    }

    public void setToStoreType(String toStoreType) {
        this.toStoreType = toStoreType;
    }

    public String getToStoreRemarks() {
        return toStoreRemarks;
    }

    public void setToStoreRemarks(String toStoreRemarks) {
        this.toStoreRemarks = toStoreRemarks;
    }

    public LocalDate getToStoreDate() {
        return toStoreDate;
    }

    public void setToStoreDate(LocalDate toStoreDate) {
        this.toStoreDate = toStoreDate;
    }

    public LocalDate getToCompanyDate() {
        return toCompanyDate;
    }

    public void setToCompanyDate(LocalDate toCompanyDate) {
        this.toCompanyDate = toCompanyDate;
    }

    public String getToCompanyRemarks() {
        return toCompanyRemarks;
    }

    public void setToCompanyRemarks(String toCompanyRemarks) {
        this.toCompanyRemarks = toCompanyRemarks;
    }

    public LocalDate getFromCompanyDate() {
        return fromCompanyDate;
    }

    public void setFromCompanyDate(LocalDate fromCompanyDate) {
        this.fromCompanyDate = fromCompanyDate;
    }

    public String getFromCompanyProductId() {
        return fromCompanyProductId;
    }

    public void setFromCompanyProductId(String fromCompanyProductId) {
        this.fromCompanyProductId = fromCompanyProductId;
    }

    public String getAreaDepotId() {
        return areaDepotId;
    }

    public void setAreaDepotId(String areaDepotId) {
        this.areaDepotId = areaDepotId;
    }

    public Boolean getFromAreaToFinance() {
        return fromAreaToFinance;
    }

    public void setFromAreaToFinance(Boolean fromAreaToFinance) {
        this.fromAreaToFinance = fromAreaToFinance;
    }

    public Boolean getToAreaToFinance() {
        return toAreaToFinance;
    }

    public void setToAreaToFinance(Boolean toAreaToFinance) {
        this.toAreaToFinance = toAreaToFinance;
    }

    public Boolean getFromCompanyToFinance() {
        return fromCompanyToFinance;
    }

    public void setFromCompanyToFinance(Boolean fromCompanyToFinance) {
        this.fromCompanyToFinance = fromCompanyToFinance;
    }

    public Boolean getToCompanyToFinance() {
        return toCompanyToFinance;
    }

    public void setToCompanyToFinance(Boolean toCompanyToFinance) {
        this.toCompanyToFinance = toCompanyToFinance;
    }

    public String getBadDepotId() {
        return badDepotId;
    }

    public void setBadDepotId(String badDepotId) {
        this.badDepotId = badDepotId;
    }

    public LocalDateTime getToFinanceDate() {
        return toFinanceDate;
    }

    public void setToFinanceDate(LocalDateTime toFinanceDate) {
        this.toFinanceDate = toFinanceDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }
}
