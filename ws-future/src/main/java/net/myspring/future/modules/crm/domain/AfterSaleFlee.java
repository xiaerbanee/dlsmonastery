package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by liuj on 2017/5/12.
 */

@Entity
@Table(name="crm_after_sale_flee")
public class AfterSaleFlee extends DataEntity<AfterSaleFlee> {
    private String ime;
    private String fleeShopName;
    private String contact;
    private String mobilePhone;
    private String address;
    private BigDecimal buyAmount;
    private String afterSaleId;

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getFleeShopName() {
        return fleeShopName;
    }

    public void setFleeShopName(String fleeShopName) {
        this.fleeShopName = fleeShopName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }
}
