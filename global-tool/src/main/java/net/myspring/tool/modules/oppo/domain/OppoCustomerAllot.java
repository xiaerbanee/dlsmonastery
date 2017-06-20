package net.myspring.tool.modules.oppo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/7/16.
 */
public class OppoCustomerAllot {
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
