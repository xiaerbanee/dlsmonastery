package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="crm_product_type")
public class ProductType extends DataEntity<ProductType> {
    private String name;
    private String reportName;
    private String code;
    private Integer version = 0;
    private BigDecimal baokaPrice;
    private BigDecimal price1;
    private BigDecimal price2;
    private BigDecimal price3;
    private Boolean scoreType;
    private List<PriceChangeCommission> priceChangeCommissionList = Lists.newArrayList();
    private List<String> priceChangeCommissionIdList = Lists.newArrayList();
    private List<PriceChangeProduct> priceChangeProductList = Lists.newArrayList();
    private List<String> priceChangeProductIdList = Lists.newArrayList();
    private List<Product> productList = Lists.newArrayList();
    private List<String> productIdList = Lists.newArrayList();
    private List<ProductImeUpload> productImeUploadList = Lists.newArrayList();
    private List<String> productImeUploadIdList = Lists.newArrayList();
    private List<ProductMonthPriceDetail> productMonthPriceDetailList = Lists.newArrayList();
    private List<String> productMonthPriceDetailIdList = Lists.newArrayList();
    private DemoPhoneType demoPhoneType;
    private String demoPhoneTypeId;
    private List<String> commissionAreaIdList = Lists.newArrayList();
    private List<String> commissionBasicIdList = Lists.newArrayList();
    private List<String> taskProductTypeDetailIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getBaokaPrice() {
        return baokaPrice;
    }

    public void setBaokaPrice(BigDecimal baokaPrice) {
        this.baokaPrice = baokaPrice;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public Boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(Boolean scoreType) {
        this.scoreType = scoreType;
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<String> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<String> productIdList) {
        this.productIdList = productIdList;
    }

    public List<ProductImeUpload> getProductImeUploadList() {
        return productImeUploadList;
    }

    public void setProductImeUploadList(List<ProductImeUpload> productImeUploadList) {
        this.productImeUploadList = productImeUploadList;
    }

    public List<String> getProductImeUploadIdList() {
        return productImeUploadIdList;
    }

    public void setProductImeUploadIdList(List<String> productImeUploadIdList) {
        this.productImeUploadIdList = productImeUploadIdList;
    }

    public List<ProductMonthPriceDetail> getProductMonthPriceDetailList() {
        return productMonthPriceDetailList;
    }

    public void setProductMonthPriceDetailList(List<ProductMonthPriceDetail> productMonthPriceDetailList) {
        this.productMonthPriceDetailList = productMonthPriceDetailList;
    }

    public List<String> getProductMonthPriceDetailIdList() {
        return productMonthPriceDetailIdList;
    }

    public void setProductMonthPriceDetailIdList(List<String> productMonthPriceDetailIdList) {
        this.productMonthPriceDetailIdList = productMonthPriceDetailIdList;
    }

    public DemoPhoneType getDemoPhoneType() {
        return demoPhoneType;
    }

    public void setDemoPhoneType(DemoPhoneType demoPhoneType) {
        this.demoPhoneType = demoPhoneType;
    }

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }

    public List<String> getCommissionAreaIdList() {
        return commissionAreaIdList;
    }

    public void setCommissionAreaIdList(List<String> commissionAreaIdList) {
        this.commissionAreaIdList = commissionAreaIdList;
    }

    public List<String> getCommissionBasicIdList() {
        return commissionBasicIdList;
    }

    public void setCommissionBasicIdList(List<String> commissionBasicIdList) {
        this.commissionBasicIdList = commissionBasicIdList;
    }

    public List<String> getTaskProductTypeDetailIdList() {
        return taskProductTypeDetailIdList;
    }

    public void setTaskProductTypeDetailIdList(List<String> taskProductTypeDetailIdList) {
        this.taskProductTypeDetailIdList = taskProductTypeDetailIdList;
    }
}
