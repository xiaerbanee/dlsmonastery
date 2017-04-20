package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Maps;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.*;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by lihx on 2017/4/17.
 */
public class DepotDto extends DataDto<Depot> {
    private Integer type;

    private String name;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String areaName;
    private String typeLabel;
    private Map<String, BigDecimal> depositMap = Maps.newHashMap();
    private String contator;
    private String mobilePhone;
    private String outGroupName;
    private String chainId;
    @CacheInput(inputKey = "chains",inputInstance = "chainId",outputInstance = "name")
    private String chainName;
    private String pricesystemId;
    @CacheInput(inputKey = "pricesystems",inputInstance = "pricesystemId",outputInstance = "name")
    private String pricesystemName;
    private Boolean adShop;
    private Boolean isHidden;
    private String areaType;
    private String delegateDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "delegateDepotId",outputInstance = "name")
    private String delegateDepotName;
    private String townType;
    private BigDecimal credit;
    private String code;
    private String outId;
    private String expressCompanyId;
    @CacheInput(inputKey = "expressCompanies",inputInstance = "expressCompanyId",outputInstance = "name")
    private String expressCompanyName;
    private Boolean rebate;
    private Boolean locked;

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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public Map<String, BigDecimal> getDepositMap() {
        return depositMap;
    }

    public void setDepositMap(Map<String, BigDecimal> depositMap) {
        this.depositMap = depositMap;
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

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getPricesystemName() {
        return pricesystemName;
    }

    public void setPricesystemName(String pricesystemName) {
        this.pricesystemName = pricesystemName;
    }

    public Boolean getAdShop() {
        return adShop;
    }

    public void setAdShop(Boolean adShop) {
        this.adShop = adShop;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getDelegateDepotId() {
        return delegateDepotId;
    }

    public void setDelegateDepotId(String delegateDepotId) {
        this.delegateDepotId = delegateDepotId;
    }

    public String getDelegateDepotName() {
        return delegateDepotName;
    }

    public void setDelegateDepotName(String delegateDepotName) {
        this.delegateDepotName = delegateDepotName;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public Boolean getRebate() {
        return rebate;
    }

    public void setRebate(Boolean rebate) {
        this.rebate = rebate;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
