package net.myspring.tool.modules.oppo.domain;

import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oppo_push_customer_employee")
public class OppoPushCustomerEmployee extends IdEntity<OppoPushCustomerEmployee>{
    private String customerId;
    private String userId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
