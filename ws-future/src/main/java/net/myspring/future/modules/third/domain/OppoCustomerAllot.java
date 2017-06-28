package net.myspring.future.modules.third.domain;

import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
@Entity
public class OppoCustomerAllot  extends IdEntity<OppoCustomerAllot> {
    private String fromCustomerid;
    private String toCustomerid;
    private String productcode;
    private Integer qty;
    private LocalDate date;

    public String getFromCustomerid() {
        return fromCustomerid;
    }

    public void setFromCustomerid(String fromCustomerid) {
        this.fromCustomerid = fromCustomerid;
    }

    public String getToCustomerid() {
        return toCustomerid;
    }

    public void setToCustomerid(String toCustomerid) {
        this.toCustomerid = toCustomerid;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
