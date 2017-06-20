package net.myspring.tool.modules.oppo.domain;

import java.time.LocalDate;

public class OppoCustomerSale {
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
