package net.myspring.future.modules.basic.domain;

import net.myspring.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="crm_depot")
public class Depot extends TreeEntity<Depot> {
    private String code;
    private Integer type;
    private String name;
    private String areaId;
    private String provinceId;
    private String namePinyin;
    private String contator;
    private String mobilePhone;
    private String address;
    private String areaType;
    private Boolean printPrice;
    private Integer version;
    private Boolean hasGuide;
    private String printType;
    private Boolean rebate;
    private Boolean allowAdApply;
    private String outId;
    private String outType;
    private String outGroupId;
    private String outGroupName;
    private LocalDateTime outDate;
    private String lng;
    private String lat;
    private String delegateDepotId;
    private BigDecimal credit;
    private String oldName;
    private Boolean checkStock;
    private String townType;
    private String chainType;
    private String carrierType;
    private String turnoverType;
    private String businessType;
    private String channelType;
    private String salePointType;
    private Boolean bussinessCenter;
    private String bussinessCenterName;
    private Boolean doorHead;
    private Boolean specialityStore;
    private String specialityStoreType;
    private String shopArea;
    private Integer frameNum;
    private Integer deskDoubleNum;
    private Integer deskSingleNum;
    private Integer cabinetNum;
    private LocalDateTime enableDate;
    private Boolean isHidden;
    private String townName;
    private Boolean adShop;
    private Boolean adShopBsc;
    private String oldOutId;
    private String taxName;
    private String cmccCarrierShopId;
    private String ctccCarrierShopId;
    private String officeId;
    private String dealerId;
    private String chainId;
    private String pricesystemId;
    private String districtId;
    private String adPricesystemId;
    private String parentId;
    private Long taskQty;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Boolean getPrintPrice() {
        return printPrice;
    }

    public void setPrintPrice(Boolean printPrice) {
        this.printPrice = printPrice;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getHasGuide() {
        return hasGuide;
    }

    public void setHasGuide(Boolean hasGuide) {
        this.hasGuide = hasGuide;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public Boolean getRebate() {
        return rebate;
    }

    public void setRebate(Boolean rebate) {
        this.rebate = rebate;
    }

    public Boolean getAllowAdApply() {
        return allowAdApply;
    }

    public void setAllowAdApply(Boolean allowAdApply) {
        this.allowAdApply = allowAdApply;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getOutGroupId() {
        return outGroupId;
    }

    public void setOutGroupId(String outGroupId) {
        this.outGroupId = outGroupId;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public LocalDateTime getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDateTime outDate) {
        this.outDate = outDate;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDelegateDepotId() {
        return delegateDepotId;
    }

    public void setDelegateDepotId(String delegateDepotId) {
        this.delegateDepotId = delegateDepotId;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public Boolean getCheckStock() {
        return checkStock;
    }

    public void setCheckStock(Boolean checkStock) {
        this.checkStock = checkStock;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
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

    public LocalDateTime getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(LocalDateTime enableDate) {
        this.enableDate = enableDate;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public Boolean getAdShop() {
        return adShop;
    }

    public void setAdShop(Boolean adShop) {
        this.adShop = adShop;
    }

    public Boolean getAdShopBsc() {
        return adShopBsc;
    }

    public void setAdShopBsc(Boolean adShopBsc) {
        this.adShopBsc = adShopBsc;
    }

    public String getOldOutId() {
        return oldOutId;
    }

    public void setOldOutId(String oldOutId) {
        this.oldOutId = oldOutId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getCmccCarrierShopId() {
        return cmccCarrierShopId;
    }

    public void setCmccCarrierShopId(String cmccCarrierShopId) {
        this.cmccCarrierShopId = cmccCarrierShopId;
    }

    public String getCtccCarrierShopId() {
        return ctccCarrierShopId;
    }

    public void setCtccCarrierShopId(String ctccCarrierShopId) {
        this.ctccCarrierShopId = ctccCarrierShopId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Long getTaskQty() {
        return taskQty;
    }

    public void setTaskQty(Long taskQty) {
        this.taskQty = taskQty;
    }
}
