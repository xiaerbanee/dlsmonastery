package net.myspring.tool.modules.oppo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/8/11.
 */
@Entity
@Table(name="oppo_push_customer_operator_type")
public class OppoCustomerOperatortype {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String customerid;
    private String customername;
    private String operatortype;
    @JsonIgnore
    private LocalDateTime createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getOperatortype() {
        return operatortype;
    }

    public void setOperatortype(String operatortype) {
        this.operatortype = operatortype;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
