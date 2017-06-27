package net.myspring.tool.common.dto;

import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by guolm on 2017/6/10.
 */
public class CustomerDto {
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "areaId")
    private String areaId;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String areaName;
    private String depotId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "depotStoreId")
    private String storeId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "depotShopId")
    private String shopId;
    private String jointLeavel;
    private String salePointType;
    private String mobilePhone;
    private String areaType;
    private String bussinessCenterName;
    private String chainType;
    private String carrierType;
    private String channelType;
    private String doorHead;
    private String enableDate;
    private Boolean enabled;
    private Boolean isHidden;
    private String districtId;
    private String townName;
    private String shopArea;
    private String frameNum;
    private String deskDoubleNum;
    private String deskSingleNum;
    private String cabinetNum;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getSalePointType() {
        return salePointType;
    }

    public void setSalePointType(String salePointType) {
        this.salePointType = salePointType;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getBussinessCenterName() {
        return bussinessCenterName;
    }

    public void setBussinessCenterName(String bussinessCenterName) {
        this.bussinessCenterName = bussinessCenterName;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    public String getDoorHead() {
        return doorHead;
    }

    public void setDoorHead(String doorHead) {
        this.doorHead = doorHead;
    }

    public String getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(String enableDate) {
        this.enableDate = enableDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea;
    }

    public String getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum;
    }

    public String getDeskDoubleNum() {
        return deskDoubleNum;
    }

    public void setDeskDoubleNum(String deskDoubleNum) {
        this.deskDoubleNum = deskDoubleNum;
    }

    public String getDeskSingleNum() {
        return deskSingleNum;
    }

    public void setDeskSingleNum(String deskSingleNum) {
        this.deskSingleNum = deskSingleNum;
    }

    public String getCabinetNum() {
        return cabinetNum;
    }

    public void setCabinetNum(String cabinetNum) {
        this.cabinetNum = cabinetNum;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getJointLeavel() {
        return jointLeavel;
    }

    public void setJointLeavel(String jointLeavel) {
        this.jointLeavel = jointLeavel;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
