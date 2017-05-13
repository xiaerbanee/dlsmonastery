package net.myspring.future.modules.basic.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_depot")
public class Depot extends CompanyEntity<Depot> {
    //编码
    private String code;
    //对应store_id
    private String depotStoreId;
    //对应shop_id
    private String depotShopId;
    // 名称
    private String name;
    // 拼音
    private String namePinyin;
    // 部门
    private String officeId;
    // 负责人【货品收货人】
    private String contator;
    // 手机号
    private String mobilePhone;
    // 地址
    private String address;
    // 省市区
    private String districtId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepotStoreId() {
        return depotStoreId;
    }

    public void setDepotStoreId(String depotStoreId) {
        this.depotStoreId = depotStoreId;
    }

    public String getDepotShopId() {
        return depotShopId;
    }

    public void setDepotShopId(String depotShopId) {
        this.depotShopId = depotShopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }
}
