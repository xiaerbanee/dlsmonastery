package net.myspring.general.modules.sys.dto;


import net.myspring.common.dto.DataDto;
import net.myspring.common.dto.IdDto;
import net.myspring.general.modules.sys.domain.District;

/**
 * Created by admin on 2017/4/5.
 */
public class DistrictDto extends IdDto<District> {

    private String name;
    private String parentId;
    private String shortName;
    private Integer level;
    private String cityCode;
    private String zipCode;
    private String lng;
    private String lat;
    private String namePinyin;
    private String shortNamePinyin;
    private String province;
    private String city;
    private String county;
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getShortNamePinyin() {
        return shortNamePinyin;
    }

    public void setShortNamePinyin(String shortNamePinyin) {
        this.shortNamePinyin = shortNamePinyin;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String refreshFullName(){
        fullName = province +", "+ city+", " + county+"("+getId()+")";
        return fullName;
    }
}
