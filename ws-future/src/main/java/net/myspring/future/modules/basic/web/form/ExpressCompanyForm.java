package net.myspring.future.modules.basic.web.form;


import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class ExpressCompanyForm extends BaseForm<ExpressCompany> {

    @NotEmpty(message="expressCompanyForm.nameNotEmptyMessage")
    @Length(max=64, message="expressCompanyForm.nameTooLongMessage")
    private String  name;

    private String  expressType;

    private String  districtId;

    private String  reachPlace;

    private String  shouldGetRule;
    private String  address;
    private String  phone;
    private String  mobilePhone;
    private String  contator;

    private List<String> expressTypeList= Lists.newArrayList();

    public List<String> getExpressTypeList() {
        return expressTypeList;
    }

    public void setExpressTypeList(List<String> expressTypeList) {
        this.expressTypeList = expressTypeList;
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

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getReachPlace() {
        return reachPlace;
    }

    public void setReachPlace(String reachPlace) {
        this.reachPlace = reachPlace;
    }

    public String getShouldGetRule() {
        return shouldGetRule;
    }

    public void setShouldGetRule(String shouldGetRule) {
        this.shouldGetRule = shouldGetRule;
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
}
