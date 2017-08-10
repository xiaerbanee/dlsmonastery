package net.myspring.tool.modules.oppo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="oppo_push_customer_sale")
public class OppoCustomerSale extends IdEntity<OppoCustomerSale>{
    private String customerid;
    private LocalDateTime date;
    private Long totalSaleQty;
    private String companyName;
    @JsonIgnore
    private LocalDate createdDate;
    @JsonIgnore
    public String getId() {
        return id;
    }
    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getTotalSaleQty() {
        return totalSaleQty;
    }

    public void setTotalSaleQty(Long totalSaleQty) {
        this.totalSaleQty = totalSaleQty;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
