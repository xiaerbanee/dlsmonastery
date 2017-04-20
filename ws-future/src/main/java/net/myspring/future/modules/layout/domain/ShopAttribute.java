package net.myspring.future.modules.layout.domain;


import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_shop_attribute")
public class ShopAttribute extends CompanyEntity<ShopAttribute> {
    private String shopId;
    private String typeName;
    private Double typeValue;
    private Integer version = 0;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(Double typeValue) {
        this.typeValue = typeValue;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
