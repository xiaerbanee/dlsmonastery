package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name="crm_product_ime_upload")
public class ProductImeUpload extends CompanyEntity<ProductImeUpload> {
    private String month;
    private String shopId;
    private String status;
    private Integer version = 0;
    private BigDecimal baokaPrice;
    private String oldIme;

    private String employeeId;

    private String productImeId;

    private String productTypeId;
    private String saleShopId;

    public String getSaleShopId() {
        return saleShopId;
    }

    public void setSaleShopId(String saleShopId) {
        this.saleShopId = saleShopId;
    }

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

}
