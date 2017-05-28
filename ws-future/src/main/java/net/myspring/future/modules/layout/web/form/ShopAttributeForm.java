package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import net.myspring.future.modules.layout.dto.ShopAttributeDetailDto;

import java.util.List;

/**
 * Created by wangzm on 2017/5/11.
 */
public class ShopAttributeForm extends BaseForm<ShopAttribute> {

    private String shopId;
    private Depot shop;
    private Long shopMonthTotal;
    private List<ShopAttributeDetailDto> shopAttributeDetailList= Lists.newArrayList();

    public Long getShopMonthTotal() {
        return shopMonthTotal;
    }

    public void setShopMonthTotal(Long shopMonthTotal) {
        this.shopMonthTotal = shopMonthTotal;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }

    public List<ShopAttributeDetailDto> getShopAttributeDetailList() {
        return shopAttributeDetailList;
    }

    public void setShopAttributeDetailList(List<ShopAttributeDetailDto> shopAttributeDetailList) {
        this.shopAttributeDetailList = shopAttributeDetailList;
    }
}
