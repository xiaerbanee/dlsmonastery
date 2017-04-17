package net.myspring.future.modules.basic.domain;


import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="crm_ad_pricesystem")
public class AdPricesystem extends DataEntity<AdPricesystem> {
    private String name;
    private Integer version = 0;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
