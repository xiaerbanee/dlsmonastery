package net.myspring.tool.modules.vivo.domain;


import javax.persistence.*;
import java.time.LocalDate;

//代理商表
@Entity
@Table(name = "S_Customers_M13E00")
public class SCustomersM13e00 {
    @Id
    private String customerid;
    private String customername;
    private String shortcut;
    private String customertype;
    private String customersort;
    private String customerkasort;
    private String address;
    private String postcode;
    private String telephone;
    private String fax;
    private String linkman;
    private String linktel;
    private String remark;
    private String inuse;
    private String zoneid;
    private String companyid;
    private LocalDate recorddate;
    private Integer customerlevel;
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
    private String latentcustomers;

    @Column(name = "CustomerID")
    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    @Column(name = "CustomerName")
    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    @Column(name = "ShortCut")
    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    @Column(name = "CustomerType")
    public String getCustomertype() {
        return customertype;
    }

    public void setCustomertype(String customertype) {
        this.customertype = customertype;
    }

    @Column(name = "CustomerSort")
    public String getCustomersort() {
        return customersort;
    }

    public void setCustomersort(String customersort) {
        this.customersort = customersort;
    }

    @Column(name = "CustomerKASort")
    public String getCustomerkasort() {
        return customerkasort;
    }

    public void setCustomerkasort(String customerkasort) {
        this.customerkasort = customerkasort;
    }

    @Column(name = "Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "Postcode")
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "Telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(name = "Fax")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "LinkMan")
    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    @Column(name = "LinkTel")
    public String getLinktel() {
        return linktel;
    }

    public void setLinktel(String linktel) {
        this.linktel = linktel;
    }

    @Column(name = "Remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "InUse")
    public String getInuse() {
        return inuse;
    }

    public void setInuse(String inuse) {
        this.inuse = inuse;
    }

    @Column(name = "ZoneID")
    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }

    @Column(name = "CompanyID")
    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    @Column(name = "RecordDate")
    public LocalDate getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(LocalDate recorddate) {
        this.recorddate = recorddate;
    }

    @Column(name = "CustomerLevel")
    public Integer getCustomerlevel() {
        return customerlevel;
    }

    public void setCustomerlevel(Integer customerlevel) {
        this.customerlevel = customerlevel;
    }

    @Column(name = "manager")
    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @Column(name = "Customerstr1")
    public String getCustomerstr1() {
        return customerstr1;
    }

    public void setCustomerstr1(String customerstr1) {
        this.customerstr1 = customerstr1;
    }

    @Column(name = "Customerstr2")
    public String getCustomerstr2() {
        return customerstr2;
    }

    public void setCustomerstr2(String customerstr2) {
        this.customerstr2 = customerstr2;
    }

    @Column(name = "Customerstr3")
    public String getCustomerstr3() {
        return customerstr3;
    }

    public void setCustomerstr3(String customerstr3) {
        this.customerstr3 = customerstr3;
    }

    @Column(name = "Customerstr4")
    public String getCustomerstr4() {
        return customerstr4;
    }

    public void setCustomerstr4(String customerstr4) {
        this.customerstr4 = customerstr4;
    }

    @Column(name = "Customerstr5")
    public String getCustomerstr5() {
        return customerstr5;
    }

    public void setCustomerstr5(String customerstr5) {
        this.customerstr5 = customerstr5;
    }

    @Column(name = "Customerstr6")
    public String getCustomerstr6() {
        return customerstr6;
    }

    public void setCustomerstr6(String customerstr6) {
        this.customerstr6 = customerstr6;
    }

    @Column(name = "Customerstr7")
    public String getCustomerstr7() {
        return customerstr7;
    }

    public void setCustomerstr7(String customerstr7) {
        this.customerstr7 = customerstr7;
    }

    @Column(name = "Customerstr8")
    public String getCustomerstr8() {
        return customerstr8;
    }

    public void setCustomerstr8(String customerstr8) {
        this.customerstr8 = customerstr8;
    }

    @Column(name = "Customerstr9")
    public String getCustomerstr9() {
        return customerstr9;
    }

    public void setCustomerstr9(String customerstr9) {
        this.customerstr9 = customerstr9;
    }

    @Column(name = "Customerstr10")
    public String getCustomerstr10() {
        return customerstr10;
    }

    public void setCustomerstr10(String customerstr10) {
        this.customerstr10 = customerstr10;
    }

    @Column(name = "LatentCustomers")
    public String getLatentcustomers() {
        return latentcustomers;
    }

    public void setLatentcustomers(String latentcustomers) {
        this.latentcustomers = latentcustomers;
    }
}