package net.myspring.future.modules.third.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class OppoCustomerStock extends IdEntity<OppoCustomerAfterSaleImei>{
    private String customerid;
    private LocalDateTime date;
    private Integer qty;
    private String productcode;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
