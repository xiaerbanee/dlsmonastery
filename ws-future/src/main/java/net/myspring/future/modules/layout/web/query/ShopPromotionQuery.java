package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDateTime;

/**
 * Created by zhangyf on 2017/5/4.
 */
public class ShopPromotionQuery extends BaseQuery {
    private String businessId;
    private String activityType;
    private String shopName;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;


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

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(LocalDateTime createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

    public void setCreatedDateEnd(LocalDateTime createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
