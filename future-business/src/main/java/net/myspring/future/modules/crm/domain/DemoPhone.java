package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_demo_phone")
public class DemoPhone extends AuditEntity<DemoPhone> {
    private String shopId;
    private Integer version = 0;
    private ProductIme productIme;
    private String productImeId;
    private String employeeId;
    private DemoPhoneType demoPhoneType;
    private String demoPhoneTypeId;
    private Depot shop;

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
}
