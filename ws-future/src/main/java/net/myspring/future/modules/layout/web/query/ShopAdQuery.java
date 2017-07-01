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
    private String shopId;
    private String officeId;
    private String shopAdTypeId;
    private Boolean specialArea;
    private String createdBy;
    private String createdDate;
    private String processStatus;
    private LocalDate createdDateStart;
    private LocalDate createdDateEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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
        } else if(createdDateStart!=null){
            return createdDateStart;
        }
        return null;
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else if(createdDateEnd!=null){
            return createdDateEnd.plusDays(1);
        }
        return null;
    }

    public void setCreatedDateEnd(LocalDate createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }

    public void setCreatedDateStart(LocalDate createdDateStart) {
        this.createdDateStart = createdDateStart;
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
