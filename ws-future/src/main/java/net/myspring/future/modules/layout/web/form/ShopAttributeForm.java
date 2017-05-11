package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import net.myspring.future.modules.layout.dto.ShopAttributeDetailDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/5/11.
 */
public class ShopAttributeForm extends DataForm<ShopAttribute> {

    private String shopId;
    private Depot shop;
    private List<ShopAttributeDetailDto> shopAttributeDetailList= Lists.newArrayList();

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
