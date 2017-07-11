package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/4.
 */
public class ShopPromotionQuery extends BaseQuery {
    private String businessId;
    private String activityType;
    private String shopName;
    private LocalDate createdDateStart;
    private LocalDate createdDateEnd;

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

    public LocalDate getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(LocalDate createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public LocalDate getCreatedDateEnd() {
        if(createdDateEnd!=null){
            return createdDateEnd.plusDays(1);
        }
        return null;
    }

    public void setCreatedDateEnd(LocalDate createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
