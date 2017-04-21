package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.NameValueDto;
import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.BasicDistrictDto;
import net.myspring.future.modules.layout.domain.ShopAttribute;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotForm extends DataForm<Depot>{
    private Integer type;
    private String officeId;
    private String name;
    private String taxName;
    private String reportName;
    private String parentId;
    private String contator;
    private String mobilePhone;
    private String address;
    private List<String> accountIdList = Lists.newArrayList();
    private String areaType;
    private String bussinessCenter;
    private String bussinessCenterName;
    private String channelType;
    private String salePointType;
    private String specialityStoreType;
    private String chainType;
    private String carrierType;
    private String turnoverType;
    private String doorHead;
    private String enableDate;
    private String dealerId;
    private String pricesystemId;
    private String chainId;
    private String specialityStore;
    private String shopArea;
    private Integer frameNum;
    private Integer cabinetNum;
    private Integer deskDoubleNum;
    private Integer deskSingleNum;
    private String townId;
    private String districtId;
    private String cmccCarrierShopId;
    private String ctccCarrierShopId;
    private Boolean hasGuide;
    private Boolean rebate;
    private Boolean printPrice;
    private Boolean isHidden;
    private Boolean adShop;
    private Boolean adShopBsc;
    private List<NameValueDto> channelTypeList;
    private List<NameValueDto> areaTypeList;
    private List<NameValueDto> carrierTypeList;
    private List<NameValueDto> chainTypeList;
    private List<NameValueDto> turnoverTypeList;
    private List<NameValueDto> salePointTypeList;
    private List<NameValueDto> shopAreaTypeList;
    private List<NameValueDto> businessCenterTypeList;
    private List<NameValueDto> shopMonthTotalList;
    private List<NameValueDto> specialityStoreTypeList;
    private Map<Boolean,String> boolMap;
    private List<NameValueDto> typeList;
    private List<Pricesystem> pricesystemList;
    private List<Chain> chainList;
    private List<BasicDistrictDto> districtDtoList;
    private List<ShopAttribute> shopAttributeList;


    public List<ShopAttribute> getShopAttributeList() {
        return shopAttributeList;
    }

    public void setShopAttributeList(List<ShopAttribute> shopAttributeList) {
        this.shopAttributeList = shopAttributeList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getBussinessCenter() {
        return bussinessCenter;
    }

    public void setBussinessCenter(String bussinessCenter) {
        this.bussinessCenter = bussinessCenter;
    }

    public String getBussinessCenterName() {
        return bussinessCenterName;
    }

    public void setBussinessCenterName(String bussinessCenterName) {
        this.bussinessCenterName = bussinessCenterName;
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

    public String getSpecialityStoreType() {
        return specialityStoreType;
    }

    public void setSpecialityStoreType(String specialityStoreType) {
        this.specialityStoreType = specialityStoreType;
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

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getSpecialityStore() {
        return specialityStore;
    }

    public void setSpecialityStore(String specialityStore) {
        this.specialityStore = specialityStore;
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

    public Integer getCabinetNum() {
        return cabinetNum;
    }

    public void setCabinetNum(Integer cabinetNum) {
        this.cabinetNum = cabinetNum;
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

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
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

    public Boolean getHasGuide() {
        return hasGuide;
    }

    public void setHasGuide(Boolean hasGuide) {
        this.hasGuide = hasGuide;
    }

    public Boolean getRebate() {
        return rebate;
    }

    public void setRebate(Boolean rebate) {
        this.rebate = rebate;
    }

    public Boolean getPrintPrice() {
        return printPrice;
    }

    public void setPrintPrice(Boolean printPrice) {
        this.printPrice = printPrice;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
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

    public List<NameValueDto> getChannelTypeList() {
        return channelTypeList;
    }

    public void setChannelTypeList(List<NameValueDto> channelTypeList) {
        this.channelTypeList = channelTypeList;
    }

    public List<NameValueDto> getAreaTypeList() {
        return areaTypeList;
    }

    public void setAreaTypeList(List<NameValueDto> areaTypeList) {
        this.areaTypeList = areaTypeList;
    }

    public List<NameValueDto> getCarrierTypeList() {
        return carrierTypeList;
    }

    public void setCarrierTypeList(List<NameValueDto> carrierTypeList) {
        this.carrierTypeList = carrierTypeList;
    }

    public List<NameValueDto> getChainTypeList() {
        return chainTypeList;
    }

    public void setChainTypeList(List<NameValueDto> chainTypeList) {
        this.chainTypeList = chainTypeList;
    }

    public List<NameValueDto> getTurnoverTypeList() {
        return turnoverTypeList;
    }

    public void setTurnoverTypeList(List<NameValueDto> turnoverTypeList) {
        this.turnoverTypeList = turnoverTypeList;
    }

    public List<NameValueDto> getSalePointTypeList() {
        return salePointTypeList;
    }

    public void setSalePointTypeList(List<NameValueDto> salePointTypeList) {
        this.salePointTypeList = salePointTypeList;
    }

    public List<NameValueDto> getShopAreaTypeList() {
        return shopAreaTypeList;
    }

    public void setShopAreaTypeList(List<NameValueDto> shopAreaTypeList) {
        this.shopAreaTypeList = shopAreaTypeList;
    }

    public List<NameValueDto> getBusinessCenterTypeList() {
        return businessCenterTypeList;
    }

    public void setBusinessCenterTypeList(List<NameValueDto> businessCenterTypeList) {
        this.businessCenterTypeList = businessCenterTypeList;
    }

    public List<NameValueDto> getShopMonthTotalList() {
        return shopMonthTotalList;
    }

    public void setShopMonthTotalList(List<NameValueDto> shopMonthTotalList) {
        this.shopMonthTotalList = shopMonthTotalList;
    }

    public List<NameValueDto> getSpecialityStoreTypeList() {
        return specialityStoreTypeList;
    }

    public void setSpecialityStoreTypeList(List<NameValueDto> specialityStoreTypeList) {
        this.specialityStoreTypeList = specialityStoreTypeList;
    }

    public Map<Boolean,String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<Boolean,String> boolMap) {
        this.boolMap = boolMap;
    }

    public List<NameValueDto> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<NameValueDto> typeList) {
        this.typeList = typeList;
    }

    public List<Pricesystem> getPricesystemList() {
        return pricesystemList;
    }

    public void setPricesystemList(List<Pricesystem> pricesystemList) {
        this.pricesystemList = pricesystemList;
    }

    public List<Chain> getChainList() {
        return chainList;
    }

    public void setChainList(List<Chain> chainList) {
        this.chainList = chainList;
    }

    public List<BasicDistrictDto> getDistrictDtoList() {
        return districtDtoList;
    }

    public void setDistrictDtoList(List<BasicDistrictDto> districtDtoList) {
        this.districtDtoList = districtDtoList;
    }
}
