package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.DataEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_shop_ad_type")
public class ShopAdType extends DataEntity<ShopAdType> {
    private String name;
    private String totalPriceType;
    private BigDecimal price;
    private Integer version = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPriceType() {
        return totalPriceType;
    }

    public void setTotalPriceType(String totalPriceType) {
        this.totalPriceType = totalPriceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
