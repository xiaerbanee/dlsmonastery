package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DepotShop;

import java.time.LocalDate;

/**
 * Created by wangzm on 2017/5/13.
 */
public class DepotShopDto extends DataDto<DepotShop>{

    private String depotId;
    // 地区属性
    private String areaType;

    private Boolean hasGuide;

    //门店各种属性
    //运营商营业厅类型
    private String carrierType;
    //营业额分类
    private String turnoverType;
    //经营方式
    private String businessType;
    //运营商类型移动社会渠道
    private String channelType;
    //门店类型
    private String salePointType;
    //乡镇类型
    private String townType;
    //面积大小
    private String shopArea;

    private String depotName;

    private String townId;
    private LocalDate enableDate;
    private String townName;

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

    public LocalDate getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(LocalDate enableDate) {
        this.enableDate = enableDate;
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

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }
}
