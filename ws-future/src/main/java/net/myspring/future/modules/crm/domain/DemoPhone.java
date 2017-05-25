package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_demo_phone")
public class DemoPhone extends AuditEntity<DemoPhone> {
    private String shopId;
    private Integer version = 0;

    private String productImeId;
    private String employeeId;

    private String demoPhoneTypeId;


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

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }
}
