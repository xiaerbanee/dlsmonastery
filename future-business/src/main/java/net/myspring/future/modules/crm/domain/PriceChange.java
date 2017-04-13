package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="crm_price_change")
public class PriceChange extends DataEntity<PriceChange> {
    private String name;
    private LocalDate priceChangeDate;
    private LocalDate uploadEndDate;
    private Integer checkPercent;
    private Integer version = 0;
    private String status;
    private List<PriceChangeCommission> priceChangeCommissionList = Lists.newArrayList();
    private List<String> priceChangeCommissionIdList = Lists.newArrayList();
    private List<PriceChangeIme> priceChangeImeList = Lists.newArrayList();
    private List<String> priceChangeImeIdList = Lists.newArrayList();
    private List<PriceChangeProduct> priceChangeProductList = Lists.newArrayList();
    private List<String> priceChangeProductIdList = Lists.newArrayList();

    private List<ProductType> productTypeList = Lists.newArrayList();
    private List<String> productTypeIdList = Lists.newArrayList();

    @Transient
    private String productTypeNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPriceChangeDate() {
        return priceChangeDate;
    }

    public void setPriceChangeDate(LocalDate priceChangeDate) {
        this.priceChangeDate = priceChangeDate;
    }

    public LocalDate getUploadEndDate() {
        return uploadEndDate;
    }

    public void setUploadEndDate(LocalDate uploadEndDate) {
        this.uploadEndDate = uploadEndDate;
    }

    public Integer getCheckPercent() {
        return checkPercent;
    }

    public void setCheckPercent(Integer checkPercent) {
        this.checkPercent = checkPercent;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PriceChangeCommission> getPriceChangeCommissionList() {
        return priceChangeCommissionList;
    }

    public void setPriceChangeCommissionList(List<PriceChangeCommission> priceChangeCommissionList) {
        this.priceChangeCommissionList = priceChangeCommissionList;
    }

    public List<String> getPriceChangeCommissionIdList() {
        return priceChangeCommissionIdList;
    }

    public void setPriceChangeCommissionIdList(List<String> priceChangeCommissionIdList) {
        this.priceChangeCommissionIdList = priceChangeCommissionIdList;
    }

    public List<PriceChangeIme> getPriceChangeImeList() {
        return priceChangeImeList;
    }

    public void setPriceChangeImeList(List<PriceChangeIme> priceChangeImeList) {
        this.priceChangeImeList = priceChangeImeList;
    }

    public List<String> getPriceChangeImeIdList() {
        return priceChangeImeIdList;
    }

    public void setPriceChangeImeIdList(List<String> priceChangeImeIdList) {
        this.priceChangeImeIdList = priceChangeImeIdList;
    }

    public List<PriceChangeProduct> getPriceChangeProductList() {
        return priceChangeProductList;
    }

    public void setPriceChangeProductList(List<PriceChangeProduct> priceChangeProductList) {
        this.priceChangeProductList = priceChangeProductList;
    }

    public List<String> getPriceChangeProductIdList() {
        return priceChangeProductIdList;
    }

    public void setPriceChangeProductIdList(List<String> priceChangeProductIdList) {
        this.priceChangeProductIdList = priceChangeProductIdList;
    }

    public String getProductTypeNames() {
        return productTypeNames;
    }

    public void setProductTypeNames(String productTypeNames) {
        this.productTypeNames = productTypeNames;
    }

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
    }
}
