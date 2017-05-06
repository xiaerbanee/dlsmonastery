package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by zhangyf on 2017/5/6.
 */
public class ShopBuildQuery {
    private String officeId;
    private String auditType;
    private String positionId;
    private String shopName;
    private String processStatus;
    private String fixtureType;
    private String createdBy;
    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        this.createdDateRange = createdDateRange;

    }

    public void setAuditType(String auditType){
        if(auditType.equalsIgnoreCase("1")){
            this.positionId = SecurityUtils.getPositionId();
        }else{
            this.positionId = null;
        }
        this.auditType = auditType;
    }

    public String getOfficeId() {
        return officeId;
    }


    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getFixtureType() {
        return fixtureType;
    }

    public void setFixtureType(String fixtureType) {
        this.fixtureType = fixtureType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

}
