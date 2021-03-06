package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.ExpressCompany;

/**
 * Created by lihx on 2017/4/17.
 */
public class ExpressCompanyDto extends DataDto<ExpressCompany> {
    private String name;
    private String expressType;
    private String reachPlace;
    private String address;
    private String phone;
    private String mobilePhone;
    private String contator;
    private String shouldGetRule;
    private String  districtId;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

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

}
