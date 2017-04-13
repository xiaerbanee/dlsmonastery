package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_demo_phone_type_office")
public class DemoPhoneTypeOffice extends IdEntity<DemoPhoneTypeOffice> {
    private Integer qty;
    private DemoPhoneType demoPhoneType;
    private String demoPhoneTypeId;
    private String officeId;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
