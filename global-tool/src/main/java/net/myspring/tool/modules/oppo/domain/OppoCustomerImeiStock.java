package net.myspring.tool.modules.oppo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/7/16.
 */
public class OppoCustomerImeiStock {

    private String customerid;
    private LocalDate date;
    private String imei;
    private String productcode;
    private String transType;

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
