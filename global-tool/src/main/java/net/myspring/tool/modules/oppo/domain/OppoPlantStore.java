package net.myspring.tool.modules.oppo.domain;

import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oppo_plant_store")
public class OppoPlantStore extends IdEntity<OppoPlantStore>{
    private String deptAcode;
    private String deptAdesrc;
    private String deptBcode;
    private String deptBdesrc;
    private String deptCode;
    private String deptDesrc;
    private String shopType;
    private String province;
    private String city;
    private String country;
    private String street;
    private String lastUpDdttm;
    private String phone;
    private String longitude;
    private String latitude;

    public String getDeptAcode() {
        return deptAcode;
    }

    public void setDeptAcode(String deptAcode) {
        this.deptAcode = deptAcode;
    }

    public String getDeptAdesrc() {
        return deptAdesrc;
    }

    public void setDeptAdesrc(String deptAdesrc) {
        this.deptAdesrc = deptAdesrc;
    }

    public String getDeptBcode() {
        return deptBcode;
    }

    public void setDeptBcode(String deptBcode) {
        this.deptBcode = deptBcode;
    }

    public String getDeptBdesrc() {
        return deptBdesrc;
    }

    public void setDeptBdesrc(String deptBdesrc) {
        this.deptBdesrc = deptBdesrc;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptDesrc() {
        return deptDesrc;
    }

    public void setDeptDesrc(String deptDesrc) {
        this.deptDesrc = deptDesrc;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLastUpDdttm() {
        return lastUpDdttm;
    }

    public void setLastUpDdttm(String lastUpDdttm) {
        this.lastUpDdttm = lastUpDdttm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
