package net.myspring.tool.modules.vivo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vivo_push_scustomers")
public class VivoPushScustomers {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String Id;
    @Column(name = "customerid")
    private String customerId;
    @Column(name = "customername")
    private String customerName;
    @Column(name = "shortcut")
    private String shortCut;
    @Column(name = "customertype")
    private String customerType;
    @Column(name = "customersort")
    private String customerSort;
    @Column(name = "customerkasort")
    private String customerKasort;
    @Column(name = "address")
    private String address;
    @Column(name = "postcode")
    private String postCode;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "linkman")
    private String linkman;
    @Column(name = "linktel")
    private String linktel;
    @Column(name = "remark")
    private String remark;
    @Column(name = "inuse")
    private String inUse;
    @Column(name = "zoneid")
    private String zoneId;
    @Column(name = "companyid")
    private String companyId;
    @Column(name = "recorddate")
    private String recordDate;
    @Column(name = "customerlevel")
    private String customerLevel;
    @Column(name = "manager")
    private String Manager;
    @Column(name = "customerstr1")
    private String customerStr1;
    @Column(name = "customerstr2")
    private String customerStr2;
    @Column(name = "customerstr3")
    private String customerStr3;
    @Column(name = "customerstr4")
    private String customerStr4;
    @Column(name = "customerstr5")
    private String customerStr5;
    @Column(name = "customerstr6")
    private String customerStr6;
    @Column(name = "customerstr7")
    private String customerStr7;
    @Column(name = "customerstr8")
    private String customerStr8;
    @Column(name = "customerstr9")
    private String customerStr9;
    @Column(name = "customerstr10")
    private String customerStr10;
    @Column(name = "latentcustomers")
    private String latentCustomers;
    @JsonIgnore
    @Column(name = "created_date")
    private LocalDateTime createdTime;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
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

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinktel() {
        return linktel;
    }

    public void setLinktel(String linktel) {
        this.linktel = linktel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInUse() {
        return inUse;
    }

    public void setInUse(String inUse) {
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

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String manager) {
        Manager = manager;
    }

    public String getCustomerStr1() {
        return customerStr1;
    }

    public void setCustomerStr1(String customerStr1) {
        this.customerStr1 = customerStr1;
    }

    public String getCustomerStr2() {
        return customerStr2;
    }

    public void setCustomerStr2(String customerStr2) {
        this.customerStr2 = customerStr2;
    }

    public String getCustomerStr3() {
        return customerStr3;
    }

    public void setCustomerStr3(String customerStr3) {
        this.customerStr3 = customerStr3;
    }

    public String getCustomerStr4() {
        return customerStr4;
    }

    public void setCustomerStr4(String customerStr4) {
        this.customerStr4 = customerStr4;
    }

    public String getCustomerStr5() {
        return customerStr5;
    }

    public void setCustomerStr5(String customerStr5) {
        this.customerStr5 = customerStr5;
    }

    public String getCustomerStr6() {
        return customerStr6;
    }

    public void setCustomerStr6(String customerStr6) {
        this.customerStr6 = customerStr6;
    }

    public String getCustomerStr7() {
        return customerStr7;
    }

    public void setCustomerStr7(String customerStr7) {
        this.customerStr7 = customerStr7;
    }

    public String getCustomerStr8() {
        return customerStr8;
    }

    public void setCustomerStr8(String customerStr8) {
        this.customerStr8 = customerStr8;
    }

    public String getCustomerStr9() {
        return customerStr9;
    }

    public void setCustomerStr9(String customerStr9) {
        this.customerStr9 = customerStr9;
    }

    public String getCustomerStr10() {
        return customerStr10;
    }

    public void setCustomerStr10(String customerStr10) {
        this.customerStr10 = customerStr10;
    }

    public String getLatentCustomers() {
        return latentCustomers;
    }

    public void setLatentCustomers(String latentCustomers) {
        this.latentCustomers = latentCustomers;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
