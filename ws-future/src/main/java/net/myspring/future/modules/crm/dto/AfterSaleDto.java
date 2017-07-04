package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleDto extends DataDto<AfterSale> {
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
    private Boolean fromAreaToFinance;
    private Boolean toAreaToFinance;
    private Boolean fromCompanyToFinance;
    private Boolean toCompanyToFinance;
    private String badDepotId;
    private LocalDateTime toFinanceDate;
    private String depotId;
    private String areaDepotName;

    private String badProductIme;
    private String toAreaProductName;
    private String badProductName;
    private String retailDepotName;
    private String toAreaProductIme;
    private String fromCompanyProductName;
    
    private boolean deleted=true;

    public boolean getDeleted() {
        if (getFromAreaToFinance() || getToAreaToFinance() || getFromCompanyToFinance() || getToCompanyToFinance()) {
            return false;
        }
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getFromCompanyProductName() {
        return fromCompanyProductName;
    }

    public void setFromCompanyProductName(String fromCompanyProductName) {
        this.fromCompanyProductName = fromCompanyProductName;
    }

    public String getToAreaProductIme() {
        return toAreaProductIme;
    }

    public void setToAreaProductIme(String toAreaProductIme) {
        this.toAreaProductIme = toAreaProductIme;
    }

    public String getRetailDepotName() {
        return retailDepotName;
    }

    public void setRetailDepotName(String retailDepotName) {
        this.retailDepotName = retailDepotName;
    }

    public String getBadProductName() {
        return badProductName;
    }

    public void setBadProductName(String badProductName) {
        this.badProductName = badProductName;
    }

    public String getBadProductIme() {
        return badProductIme;
    }

    public void setBadProductIme(String badProductIme) {
        this.badProductIme = badProductIme;
    }

    public String getToAreaProductName() {
        return toAreaProductName;
    }

    public void setToAreaProductName(String toAreaProductName) {
        this.toAreaProductName = toAreaProductName;
    }

    public String getAreaDepotName() {
        return areaDepotName;
    }

    public void setAreaDepotName(String areaDepotName) {
        this.areaDepotName = areaDepotName;
    }

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

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }
}
