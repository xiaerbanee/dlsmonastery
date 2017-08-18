package net.myspring.report.modules.future.web.query;


import net.myspring.report.common.query.BaseQuery;

public class ProductMonthPriceSumQuery extends BaseQuery {

    private String month;
    private String status;
    private String areaId;
    private Boolean isDetail;

    public Boolean getIsDetail() {
        return isDetail;
    }

    public void setIsDetail(Boolean detail) {
        isDetail = detail;
    }

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
