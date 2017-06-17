package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by zhangyf on 2017/5/3.
 */
public class ShopImageQuery extends BaseQuery {
    private String shopId;
    private String officeId;
    private String shopName;
    private String createdDateStart;
    private String createdDateEnd;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(String createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public String getCreatedDateEnd() {
        return createdDateEnd;
    }

    public void setCreatedDateEnd(String createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
