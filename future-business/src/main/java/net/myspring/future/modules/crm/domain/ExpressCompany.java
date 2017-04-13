package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="crm_express_company")
public class ExpressCompany extends DataEntity<ExpressCompany> {
    private String name;
    private String expressType;
    private String reachPlace;
    private String address;
    private String phone;
    private String mobilePhone;
    private String contator;
    private String shouldGetRule;
    private String shouldPayRule;
    private Integer version = 0;
    private List<Depot> depotList = Lists.newArrayList();
    private List<String> depotIdList = Lists.newArrayList();
    private String districtId;
    private List<ExpressOrder> expressOrderList = Lists.newArrayList();
    private List<String> expressOrderIdList = Lists.newArrayList();
    private List<ShopBuild> shopBuildList = Lists.newArrayList();
    private List<String> shopBuildIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public String getReachPlace() {
        return reachPlace;
    }

    public void setReachPlace(String reachPlace) {
        this.reachPlace = reachPlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getShouldGetRule() {
        return shouldGetRule;
    }

    public void setShouldGetRule(String shouldGetRule) {
        this.shouldGetRule = shouldGetRule;
    }

    public String getShouldPayRule() {
        return shouldPayRule;
    }

    public void setShouldPayRule(String shouldPayRule) {
        this.shouldPayRule = shouldPayRule;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Depot> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Depot> depotList) {
        this.depotList = depotList;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public List<ExpressOrder> getExpressOrderList() {
        return expressOrderList;
    }

    public void setExpressOrderList(List<ExpressOrder> expressOrderList) {
        this.expressOrderList = expressOrderList;
    }

    public List<String> getExpressOrderIdList() {
        return expressOrderIdList;
    }

    public void setExpressOrderIdList(List<String> expressOrderIdList) {
        this.expressOrderIdList = expressOrderIdList;
    }

    public List<ShopBuild> getShopBuildList() {
        return shopBuildList;
    }

    public void setShopBuildList(List<ShopBuild> shopBuildList) {
        this.shopBuildList = shopBuildList;
    }

    public List<String> getShopBuildIdList() {
        return shopBuildIdList;
    }

    public void setShopBuildIdList(List<String> shopBuildIdList) {
        this.shopBuildIdList = shopBuildIdList;
    }
}
