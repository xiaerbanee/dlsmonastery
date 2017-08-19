package net.myspring.report.modules.future.web.query;


import com.google.common.collect.Lists;
import net.myspring.report.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;

import java.util.List;

public class ProductMonthPriceSumQuery extends BaseQuery {

    private String month;
    private String status;
    private String areaId;
    private List<String> auditOfficeIdList= Lists.newArrayList();
    private Boolean isDetail=false;

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

    public List<String> getAuditOfficeIdList() {
        if(StringUtils.isNotBlank(areaId)){
            this.auditOfficeIdList.add(areaId);
        }
        return auditOfficeIdList;
    }

    public void setAuditOfficeIdList(List<String> auditOfficeIdList) {
        this.auditOfficeIdList = auditOfficeIdList;
    }
}
