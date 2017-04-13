package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="crm_shop_ad_type")
public class ShopAdType extends DataEntity<ShopAdType> {
    private String name;
    private String totalPriceType;
    private BigDecimal price;
    private Integer version = 0;
    private List<ShopAd> shopAdList = Lists.newArrayList();
    private List<String> shopAdIdList = Lists.newArrayList();

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

    public List<ShopAd> getShopAdList() {
        return shopAdList;
    }

    public void setShopAdList(List<ShopAd> shopAdList) {
        this.shopAdList = shopAdList;
    }

    public List<String> getShopAdIdList() {
        return shopAdIdList;
    }

    public void setShopAdIdList(List<String> shopAdIdList) {
        this.shopAdIdList = shopAdIdList;
    }
}
