package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.layout.domain.AdApply;

/**
 * Created by wangzm on 2017/4/21.
 */
public class AdApplyForm extends DataForm<AdApply> {
    private String  shopId;
    private Depot shop;

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
}
