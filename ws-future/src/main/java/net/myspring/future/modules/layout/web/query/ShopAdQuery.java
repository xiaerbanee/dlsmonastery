package net.myspring.future.modules.layout.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/10.
 */
public class ShopAdQuery extends BaseQuery{
    private String id;
    private String shopName;
    private String areaId;
    private String shopAdTypeId;
    private Boolean specialArea;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private String processStatus;
    private Boolean doorType;

    public List<String> getIdList(){
        if(StringUtils.isNotBlank(id)){
            return Arrays.asList(id.split(CharConstant.COMMA+CharConstant.VERTICAL_LINE+CharConstant.ENTER+CharConstant.VERTICAL_LINE+CharConstant.SPACE));
        }else{
            return null;
        }
    }

    public Boolean getDoorType() {
        return doorType;
    }

    public void setDoorType(Boolean doorType) {
        this.doorType = doorType;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getShopAdTypeId() {
        return shopAdTypeId;
    }

    public void setShopAdTypeId(String shopAdTypeId) {
        this.shopAdTypeId = shopAdTypeId;
    }

    public Boolean getSpecialArea() {
        return specialArea;
    }

    public void setSpecialArea(Boolean specialArea) {
        this.specialArea = specialArea;
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

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
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

    public List<String> getIds(){
        if(StringUtils.isNotBlank(id)){
           return Arrays.asList(id.split(CharConstant.COMMA+CharConstant.VERTICAL_LINE+CharConstant.SPACE
                   +CharConstant.VERTICAL_LINE+CharConstant.ENTER));
        }else{
            return null;
        }
    }
}
