package net.myspring.report.modules.future.web.query;

import net.myspring.future.common.query.BaseQuery;

public class ProductMonthPriceSumQuery extends BaseQuery {

    private String month;
    private String status;
    private String areaId;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
