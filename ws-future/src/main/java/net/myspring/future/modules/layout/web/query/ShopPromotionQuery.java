package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/4.
 */
public class ShopPromotionQuery extends BaseQuery {
    private String businessId;
    private String activityType;
    private String shopName;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
