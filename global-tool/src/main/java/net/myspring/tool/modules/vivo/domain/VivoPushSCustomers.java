package net.myspring.tool.modules.vivo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

//代理商表
@Entity
@Table(name="vivo_push_scustomers")
public class VivoPushSCustomers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String customerId;
    private String customerName;
    private String shortcut;
    private String customerType;
    private String customerSort;
    private String customerKasort;
    private String address;
    private String postcode;
    private String telephone;
    private String fax;
    private String linkMan;
    private String linkTel;
    private String remark;
    private Integer inUse;
    private String zoneId;
    private String companyId;
    private LocalDate recordDate;
    private Integer customerLevel;
    private String manager;
    private String customerstr1;
    private String customerstr2;
    private String customerstr3;
    private String customerstr4;
    private String customerstr5;
    private String customerstr6;
    private String customerstr7;
    private String customerstr8;
    private String customerstr9;
    private String customerstr10;
    private Integer latentCustomers;
    @JsonIgnore
    private LocalDate createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerSort() {
        return customerSort;
    }

    public void setCustomerSort(String customerSort) {
        this.customerSort = customerSort;
    }

    public String getCustomerKasort() {
        return customerKasort;
    }

    public void setCustomerKasort(String customerKasort) {
        this.customerKasort = customerKasort;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getInUse() {
        return inUse;
    }

    public void setInUse(Integer inUse) {
        this.inUse = inUse;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(Integer customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCustomerstr1() {
        return customerstr1;
    }

    public void setCustomerstr1(String customerstr1) {
        this.customerstr1 = customerstr1;
    }

    public String getCustomerstr2() {
        return customerstr2;
    }

    public void setCustomerstr2(String customerstr2) {
        this.customerstr2 = customerstr2;
    }

    public String getCustomerstr3() {
        return customerstr3;
    }

    public void setCustomerstr3(String customerstr3) {
        this.customerstr3 = customerstr3;
    }

    public String getCustomerstr4() {
        return customerstr4;
    }

    public void setCustomerstr4(String customerstr4) {
        this.customerstr4 = customerstr4;
    }

    public String getCustomerstr5() {
        return customerstr5;
    }

    public void setCustomerstr5(String customerstr5) {
        this.customerstr5 = customerstr5;
    }

    public String getCustomerstr6() {
        return customerstr6;
    }

    public void setCustomerstr6(String customerstr6) {
        this.customerstr6 = customerstr6;
    }

    public String getCustomerstr7() {
        return customerstr7;
    }

    public void setCustomerstr7(String customerstr7) {
        this.customerstr7 = customerstr7;
    }

    public String getCustomerstr8() {
        return customerstr8;
    }

    public void setCustomerstr8(String customerstr8) {
        this.customerstr8 = customerstr8;
    }

    public String getCustomerstr9() {
        return customerstr9;
    }

    public void setCustomerstr9(String customerstr9) {
        this.customerstr9 = customerstr9;
    }

    public String getCustomerstr10() {
        return customerstr10;
    }

    public void setCustomerstr10(String customerstr10) {
        this.customerstr10 = customerstr10;
    }

    public Integer getLatentCustomers() {
        return latentCustomers;
    }

    public void setLatentCustomers(Integer latentCustomers) {
        this.latentCustomers = latentCustomers;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}