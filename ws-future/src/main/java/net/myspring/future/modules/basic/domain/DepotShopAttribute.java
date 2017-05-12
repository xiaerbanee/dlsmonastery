package net.myspring.future.modules.basic.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liuj on 2017/5/12.
 */
@Entity
@Table(name="crm_depot_shop_attribute")
public class DepotShopAttribute extends CompanyEntity<DepotShopAttribute> {
    private String depotId;
    private String depotShopId;
    // 经度
    private BigDecimal lng;
    // 纬度
    private BigDecimal lat;
    // 有无导购
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
    //是否核心商圈
    private Boolean bussinessCenter=false;
    //核心商圈名称
    private String bussinessCenterName;
    //有无门头
    private Boolean doorHead=false;
    //开业时间
    private Date enableDate;
    //是否体验店
    private Boolean specialityStore=false;
    private String specialityStoreType;
    //面积大小
    private String shopArea;
    //2.0悬吊画框数量
    private Integer frameNum=0;
    //2.0 双面体验台数量
    private Integer deskDoubleNum=0;
    //2.0 单面体验台数量
    private Integer deskSingleNum=0;
    //展柜数量
    private Integer cabinetNum=0;

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotShopId() {
        return depotShopId;
    }

    public void setDepotShopId(String depotShopId) {
        this.depotShopId = depotShopId;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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

    public Date getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(Date enableDate) {
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
