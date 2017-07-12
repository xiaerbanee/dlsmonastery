package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

/**
 * Created by zhangyf on 2017/5/3.
 */
public class ShopImageQuery extends BaseQuery {
    private String areaId;
    private String shopName;
    private LocalDate createdDateStart;
    private LocalDate createdDateEnd;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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
        return createdDateEnd;
    }

    public void setCreatedDateEnd(LocalDate createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
