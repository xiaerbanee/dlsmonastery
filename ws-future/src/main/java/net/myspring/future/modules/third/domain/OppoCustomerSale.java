package net.myspring.future.modules.third.domain;

import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class OppoCustomerSale  extends IdEntity<OppoCustomerSale> {
    private String customerid;
    private LocalDateTime date;
    private Long totalSaleQty;

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
}
