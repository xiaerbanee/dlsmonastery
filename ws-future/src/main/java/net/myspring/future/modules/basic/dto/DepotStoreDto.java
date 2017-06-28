package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/5/13.
 */
public class DepotStoreDto extends DataDto<DepotStore>{

    private String depotId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    protected String areaName;
    private String areaId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    protected String officeName;
    private String type;
    private String depotName;
    private String officeId;
    private String taxName;
    private String delegateDepotId;
    private String delegateDepotName;
    private String storeGroup;
    private String jointLevel;
    private Boolean popShop;
    private String percentage;
    private Integer qty=0;
    private String contator;
    private String mobilePhone;

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Boolean getPopShop() {
        return popShop;
    }

    public void setPopShop(Boolean popShop) {
        this.popShop = popShop;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getDelegateDepotName() {
        return delegateDepotName;
    }

    public void setDelegateDepotName(String delegateDepotName) {
        this.delegateDepotName = delegateDepotName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getDelegateDepotId() {
        return delegateDepotId;
    }

    public void setDelegateDepotId(String delegateDepotId) {
        this.delegateDepotId = delegateDepotId;
    }

    public String getStoreGroup() {
        return storeGroup;
    }

    public void setStoreGroup(String storeGroup) {
        this.storeGroup = storeGroup;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
