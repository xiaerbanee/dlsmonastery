package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_product_month_price")
public class ProductMonthPrice extends CompanyEntity<ProductMonthPrice> {
    private String month;
    private Integer version = 0;


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
