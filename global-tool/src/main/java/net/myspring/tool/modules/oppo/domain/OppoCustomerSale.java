package net.myspring.tool.modules.oppo.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="oppo_push_customer_sale")
public class OppoCustomerSale {
    @Id
    private String customerid;
    private LocalDate date;
    private Long totalSaleQty;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTotalSaleQty() {
        return totalSaleQty;
    }

    public void setTotalSaleQty(Long totalSaleQty) {
        this.totalSaleQty = totalSaleQty;
    }
}
