package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.DepotShop;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotShopForm extends BaseForm<DepotShop> {
    private String depotId;
    private String areaType;
    private Boolean hasGuide;
    private String carrierType;
    private String turnoverType;
    private String businessType;
    private String channelType;
    private String salePointType;
    private String townType;
    private Boolean bussinessCenter=false;
    private String bussinessCenterName;
    private Boolean doorHead=false;
    private LocalDate enableDate;
    private Boolean specialityStore=false;
    private String specialityStoreType;
    private String shopArea;
    private Integer frameNum=0;
    private Integer deskDoubleNum=0;
    private Integer deskSingleNum=0;
    private Integer cabinetNum=0;
    private String townId;
    private String townName;
    private String depotName;
    private String chainType;

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    private List<String> townTypeList=Lists.newArrayList();

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public List<String> getTownTypeList() {
        return townTypeList;
    }

    public void setTownTypeList(List<String> townTypeList) {
        this.townTypeList = townTypeList;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Boolean getHasGuide() {
        return hasGuide;
    }

    public void setHasGuide(Boolean hasGuide) {
        this.hasGuide = hasGuide;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    public String getTurnoverType() {
        return turnoverType;
    }

    public void setTurnoverType(String turnoverType) {
        this.turnoverType = turnoverType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getSalePointType() {
        return salePointType;
    }

    public void setSalePointType(String salePointType) {
        this.salePointType = salePointType;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public Boolean getBussinessCenter() {
        return bussinessCenter;
    }

    public void setBussinessCenter(Boolean bussinessCenter) {
        this.bussinessCenter = bussinessCenter;
    }

    public String getBussinessCenterName() {
        return bussinessCenterName;
    }

    public void setBussinessCenterName(String bussinessCenterName) {
        this.bussinessCenterName = bussinessCenterName;
    }

    public Boolean getDoorHead() {
        return doorHead;
    }

    public void setDoorHead(Boolean doorHead) {
        this.doorHead = doorHead;
    }

    public LocalDate getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(LocalDate enableDate) {
        this.enableDate = enableDate;
    }

    public Boolean getSpecialityStore() {
        return specialityStore;
    }

    public void setSpecialityStore(Boolean specialityStore) {
        this.specialityStore = specialityStore;
    }

    public String getSpecialityStoreType() {
        return specialityStoreType;
    }

    public void setSpecialityStoreType(String specialityStoreType) {
        this.specialityStoreType = specialityStoreType;
    }

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea;
    }

    public Integer getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(Integer frameNum) {
        this.frameNum = frameNum;
    }

    public Integer getDeskDoubleNum() {
        return deskDoubleNum;
    }

    public void setDeskDoubleNum(Integer deskDoubleNum) {
        this.deskDoubleNum = deskDoubleNum;
    }

    public Integer getDeskSingleNum() {
        return deskSingleNum;
    }

    public void setDeskSingleNum(Integer deskSingleNum) {
        this.deskSingleNum = deskSingleNum;
    }

    public Integer getCabinetNum() {
        return cabinetNum;
    }

    public void setCabinetNum(Integer cabinetNum) {
        this.cabinetNum = cabinetNum;
    }
}
