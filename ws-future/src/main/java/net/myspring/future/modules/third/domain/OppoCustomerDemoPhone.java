package net.myspring.future.modules.third.domain;

import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/9/6.
 */
@Entity
public class OppoCustomerDemoPhone extends IdEntity<OppoCustomerDemoPhone> {

    private String customerid;
    private LocalDateTime date;
    private String productCode;
    private String Imei;

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
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
