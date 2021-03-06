package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/5/13.
 */
public class DepotShopDto extends DataDto<DepotShop>{

    private String depotId;
    private String areaType;
    private Boolean hasGuide;
    private String carrierType;
    private String turnoverType;
    private String businessType;
    private String channelType;
    private String salePointType;
    private String townType;
    private String shopArea;
    private Integer frameNum=0;
    private Integer deskDoubleNum=0;
    private Integer deskSingleNum=0;
    private Integer cabinetNum=0;
    private Boolean bussinessCenter=false;
    private String bussinessCenterName;
    private String depotName;
    private String townId;
    private LocalDate enableDate;
    private String townName;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String areaId;
    @CacheInput(inputKey="offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    private String contator;
    private String mobilePhone;
    private String address;
    private String chainType;
    private boolean doorHead;
    private boolean specialityStore;
    private String pricesystemName;
    private String chainName;
    private String clientName;
    private String parentName;
    private Map<String,Object> depositMap= Maps.newHashMap();

    private String accountIds;
    private List<String> accountIdList= Lists.newArrayList();
    @CacheInput(inputKey="accounts",inputInstance = "accountIdList",outputInstance = "loginName")
    private List<String> accountNameList=Lists.newArrayList();
    private String accountNameStr;

    public String getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(String accountIds) {
        this.accountIds = accountIds;
    }

    public List<String> getAccountIdList() {
        if(CollectionUtil.isEmpty(accountIdList)&&StringUtils.isNotBlank(accountIds)){
            this.accountIdList=StringUtils.getSplitList(accountIds,CharConstant.COMMA);
        }
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<String> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<String> accountNameList) {
        this.accountNameList = accountNameList;
    }

    public String getAccountNameStr() {
        if(StringUtils.isBlank(accountNameStr)&& CollectionUtil.isNotEmpty(accountNameList)){
            this.accountNameStr=StringUtils.join(accountNameList, CharConstant.COMMA);
        }
        return accountNameStr;
    }

    public void setAccountNameStr(String accountNameStr) {
        this.accountNameStr = accountNameStr;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPricesystemName() {
        return pricesystemName;
    }

    public void setPricesystemName(String pricesystemName) {
        this.pricesystemName = pricesystemName;
    }

    public Map<String, Object> getDepositMap() {
        return depositMap;
    }

    public void setDepositMap(Map<String, Object> depositMap) {
        this.depositMap = depositMap;
    }

    public boolean isDoorHead() {
        return doorHead;
    }

    public void setDoorHead(boolean doorHead) {
        this.doorHead = doorHead;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
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

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
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

    public boolean isSpecialityStore() {
        return specialityStore;
    }

    public void setSpecialityStore(boolean specialityStore) {
        this.specialityStore = specialityStore;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
