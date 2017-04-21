package net.myspring.future.modules.basic.web.Query;

import com.google.common.collect.HashBiMap;
import net.myspring.future.common.dto.NameValueDto;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.domain.Pricesystem;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotQuery {
    private String name;
    private String type;
    private String areaType;
    private String pricesystemId;
    private String contator;
    private String mobilePhone;
    private String specialityStore;
    private String specialityStoreType;
    private String officeId;
    private String chainId;
    private String adPricesystemId;
    private String expressCompanyId;
    private String districtName;
    private Boolean adShopBsc;
    private String adShop;
    private Boolean isHidden;
    private HashBiMap<Integer, String> typeList;
    private List<String> areaList;
    private List<NameValueDto> areaTypeList;
    private List<Pricesystem> pricesystemList;
    private List<NameValueDto> specialityStoreTypeList;
    private List<Chain> chainList;
    private Map<Boolean,String> bools;
    private List<AdPricesystem> adPricesystemList;
    private List<ExpressCompany> expressCompanyList;

//后台
    private List<String> officeIdList;
    private String depotName;
    private List<Integer> depoTypeList;

    public List<Integer> getDepoTypeList() {
        return depoTypeList;
    }

    public void setDepoTypeList(List<Integer> depoTypeList) {
        this.depoTypeList = depoTypeList;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<ExpressCompany> getExpressCompanyList() {
        return expressCompanyList;
    }

    public void setExpressCompanyList(List<ExpressCompany> expressCompanyList) {
        this.expressCompanyList = expressCompanyList;
    }

    public List<AdPricesystem> getAdPricesystemList() {
        return adPricesystemList;
    }

    public void setAdPricesystemList(List<AdPricesystem> adPricesystemList) {
        this.adPricesystemList = adPricesystemList;
    }

    public Map<Boolean, String> getBools() {
        return bools;
    }

    public void setBools(Map<Boolean, String> bools) {
        this.bools = bools;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
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

    public String getSpecialityStore() {
        return specialityStore;
    }

    public void setSpecialityStore(String specialityStore) {
        this.specialityStore = specialityStore;
    }

    public String getSpecialityStoreType() {
        return specialityStoreType;
    }

    public void setSpecialityStoreType(String specialityStoreType) {
        this.specialityStoreType = specialityStoreType;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Boolean getAdShopBsc() {
        return adShopBsc;
    }

    public void setAdShopBsc(Boolean adShopBsc) {
        this.adShopBsc = adShopBsc;
    }

    public String getAdShop() {
        return adShop;
    }

    public void setAdShop(String adShop) {
        this.adShop = adShop;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public HashBiMap<Integer, String> getTypeList() {
        return typeList;
    }

    public void setTypeList(HashBiMap<Integer, String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<String> areaList) {
        this.areaList = areaList;
    }

    public List<NameValueDto> getAreaTypeList() {
        return areaTypeList;
    }

    public void setAreaTypeList(List<NameValueDto> areaTypeList) {
        this.areaTypeList = areaTypeList;
    }

    public List<Pricesystem> getPricesystemList() {
        return pricesystemList;
    }

    public void setPricesystemList(List<Pricesystem> pricesystemList) {
        this.pricesystemList = pricesystemList;
    }

    public List<NameValueDto> getSpecialityStoreTypeList() {
        return specialityStoreTypeList;
    }

    public void setSpecialityStoreTypeList(List<NameValueDto> specialityStoreTypeList) {
        this.specialityStoreTypeList = specialityStoreTypeList;
    }

    public List<Chain> getChainList() {
        return chainList;
    }

    public void setChainList(List<Chain> chainList) {
        this.chainList = chainList;
    }
}
