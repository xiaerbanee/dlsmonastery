package net.myspring.future.modules.layout.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShopAllotQuery extends BaseQuery {

    private String fromShopId;
    private String toShopId;
    private String status;
    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;

    private String businessId;
    private String businessIds;
    private String businessIdInStr;

    private String auditDateRange;
    private LocalDateTime auditDateStart;
    private LocalDateTime auditDateEnd;

    private List<String> statusList = new ArrayList<String>();

    public String getBusinessIdInStr() {
        return businessIdInStr;
    }

    public String getAuditDateRange() {
        return auditDateRange;
    }

    public void setAuditDateRange(String auditDateRange) {
        if(auditDateRange!=null){
            String[] tempParamValues = auditDateRange.split(" - ");
            this.auditDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.auditDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.auditDateRange = auditDateRange;

    }

    public LocalDateTime getAuditDateStart() {
        return auditDateStart;
    }

    public LocalDateTime getAuditDateEnd() {
        return auditDateEnd;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.createdDateRange = createdDateRange;

    }


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(String businessIds) {
//        TODO 需要修改该写法
        businessIdInStr = null;
        if(businessIds != null){
            List<String> tmp= Stream.of(StringUtils.getReplaced(businessIds).split(",", -1)).map(each -> StringUtils.trimToEmpty(each)).filter(each -> StringUtils.isNotBlank(each)).collect(Collectors.toList());
             if(tmp != null || tmp.size()>0){
                 businessIdInStr = "('"+StringUtils.join(tmp, "'"+CharConstant.COMMA+"'")+"')";
             }
        }
        this.businessIds = businessIds;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }



}
