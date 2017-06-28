package net.myspring.tool.modules.oppo.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/7/16.
 */
@Entity
@Table(name="oppo_push_customer_allot")
public class OppoCustomerAllot {
    @Id
    private String fromCustomerid;
    private String toCustomerid;
    private LocalDate date;
    private Integer qty;
    private String productcode;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public String getFromCustomerid() {
        return fromCustomerid;
    }

    public void setFromCustomerid(String fromCustomerid) {
        this.fromCustomerid = fromCustomerid;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getToCustomerid() {
        return toCustomerid;
    }

    public void setToCustomerid(String toCustomerid) {
        this.toCustomerid = toCustomerid;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
