package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by zhangyf on 2017/5/3.
 */
public class ShopImageQuery extends BaseQuery {
    private String shopName;
    private String officeId;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
