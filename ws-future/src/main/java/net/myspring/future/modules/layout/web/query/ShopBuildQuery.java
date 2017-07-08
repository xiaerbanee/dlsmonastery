package net.myspring.future.modules.layout.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.enums.ShopBuildAuditEnum;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/6.
 */
public class ShopBuildQuery extends BaseQuery {
    private String areaId;
    private String idStr;
    private String auditType = ShopBuildAuditEnum.待批需要我审核.name();
    private String shopName;
    private String processStatus;
    private String fixtureType;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public void setAuditType(String auditType){
        this.auditType = auditType;
    }

    public String getAuditType() {
        return auditType;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getPositionId() {
        if(StringUtils.isNotBlank(auditType)){
            if(ShopBuildAuditEnum.待批需要我审核.name().equalsIgnoreCase(auditType)){
                return RequestUtils.getPositionId();
            }
        }
        return null;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getLastModifiedDateStart() {
        if(StringUtils.isNotBlank(lastModifiedDate)) {
            return LocalDateUtils.parse(lastModifiedDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getLastModifiedDateEnd() {
        if(StringUtils.isNotBlank(lastModifiedDate)) {
            return LocalDateUtils.parse(lastModifiedDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public List<String> getIdLists(){
        if(StringUtils.isNotBlank(idStr)){
            return Arrays.asList(idStr.split(CharConstant.COMMA+CharConstant.VERTICAL_LINE+CharConstant.ENTER+CharConstant.VERTICAL_LINE+CharConstant.SPACE));
        }else{
            return null;
        }
    }

}
