package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.ShopAllot;

import java.util.List;


public class ShopAllotForm extends BaseForm<ShopAllot> {

    private String fromShopId;
    private String toShopId;
    private List<ShopAllotDetailForm> shopAllotDetailFormList;

    public String getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(String fromShopId) {
        this.fromShopId = fromShopId;
    }

    public String getToShopId() {
        return toShopId;
    }

    public void setToShopId(String toShopId) {
        this.toShopId = toShopId;
    }

    public List<ShopAllotDetailForm> getShopAllotDetailFormList() {
        return shopAllotDetailFormList;
    }

    public void setShopAllotDetailFormList(List<ShopAllotDetailForm> shopAllotDetailFormList) {
        this.shopAllotDetailFormList = shopAllotDetailFormList;
    }
}
