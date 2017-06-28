package net.myspring.future.modules.third.domain;

import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/9/6.
 */
@Entity
public class OppoCustomerAfterSaleImei extends IdEntity<OppoCustomerAfterSaleImei> {

    private String customerid;
    private LocalDateTime date;
    private String productCode;
    private String imei;
    private String transType;

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
