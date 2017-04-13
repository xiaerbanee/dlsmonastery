package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="crm_product_ime_upload")
public class ProductImeUpload extends DataEntity<ProductImeUpload> {
    private String month;
    private String shopId;
    private String status;
    private Integer version = 0;
    private BigDecimal baokaPrice;
    private String oldIme;
    private List<ProductIme> productImeList = Lists.newArrayList();
    private List<String> productImeIdList = Lists.newArrayList();
    private String employeeId;
    private ProductIme productIme;
    private String productImeId;
    private ProductType productType;
    private String productTypeId;
    private Depot shop;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOldIme() {
        return oldIme;
    }

    public void setOldIme(String oldIme) {
        this.oldIme = oldIme;
    }

    public List<ProductIme> getProductImeList() {
        return productImeList;
    }

    public void setProductImeList(List<ProductIme> productImeList) {
        this.productImeList = productImeList;
    }

    public List<String> getProductImeIdList() {
        return productImeIdList;
    }

    public void setProductImeIdList(List<String> productImeIdList) {
        this.productImeIdList = productImeIdList;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }
}
