package net.myspring.future.modules.basic.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="crm_depot")
public class Depot extends CompanyEntity<Depot> {
    //总店
    private String parentId;
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
    // 财务编号
    private String outId;
    //财务分组
    private Long outGroupId;
    //财务分组名称
    private String outGroupName;
    // 财务类型
    private String outType;
    //财务同步日期
    private Date outDate;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

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

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public Long getOutGroupId() {
        return outGroupId;
    }

    public void setOutGroupId(Long outGroupId) {
        this.outGroupId = outGroupId;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }
}
