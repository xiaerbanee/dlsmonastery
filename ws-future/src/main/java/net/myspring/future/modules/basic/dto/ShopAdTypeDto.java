package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.layout.domain.ShopAd;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class ShopAdTypeDto extends DataDto<ShopAdType> {
    private String name;
    private String totalPriceType;
    private BigDecimal price;
    private Integer version;
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
