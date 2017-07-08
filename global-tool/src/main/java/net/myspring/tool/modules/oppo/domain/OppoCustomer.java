package net.myspring.tool.modules.oppo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by guolm on 2016/7/16.
 */
@Entity
@Table(name="oppo_push_customer")
public class OppoCustomer extends IdEntity<OppoCustomer> {
    private String customerid;
    private String customername;
    private String agentid;
    private String companyid;
    private String dealertype;
    private String dealergrade;
    private String dealertel;
    private String citytype;
    private String bussinesscenter;
    private String chainname;
    private String saletype;
    private String doorhead;
    private String enabledate;
    private String customertype;
    private String keydealer;
    private String isenable;
    private String province;
    private String city;
    private String county;
    private String village;
    private String dealerarea;
    private String framenum;
    private String deskdoublenum;
    private String desksinglenum;
    private String cabinetnum;
    @JsonIgnore
    private LocalDate createdDate;

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getBussinesscenter() {
        return bussinesscenter;
    }

    public void setBussinesscenter(String bussinesscenter) {
        this.bussinesscenter = bussinesscenter;
    }

    public String getCabinetnum() {
        return cabinetnum;
    }

    public void setCabinetnum(String cabinetnum) {
        this.cabinetnum = cabinetnum;
    }

    public String getChainname() {
        return chainname;
    }

    public void setChainname(String chainname) {
        this.chainname = chainname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitytype() {
        return citytype;
    }

    public void setCitytype(String citytype) {
        this.citytype = citytype;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomertype() {
        return customertype;
    }

    public void setCustomertype(String customertype) {
        this.customertype = customertype;
    }

    public String getDealerarea() {
        return dealerarea;
    }

    public void setDealerarea(String dealerarea) {
        this.dealerarea = dealerarea;
    }

    public String getDealergrade() {
        return dealergrade;
    }

    public void setDealergrade(String dealergrade) {
        this.dealergrade = dealergrade;
    }

    public String getDealertel() {
        return dealertel;
    }

    public void setDealertel(String dealertel) {
        this.dealertel = dealertel;
    }

    public String getDealertype() {
        return dealertype;
    }

    public void setDealertype(String dealertype) {
        this.dealertype = dealertype;
    }

    public String getDeskdoublenum() {
        return deskdoublenum;
    }

    public void setDeskdoublenum(String deskdoublenum) {
        this.deskdoublenum = deskdoublenum;
    }

    public String getDesksinglenum() {
        return desksinglenum;
    }

    public void setDesksinglenum(String desksinglenum) {
        this.desksinglenum = desksinglenum;
    }

    public String getDoorhead() {
        return doorhead;
    }

    public void setDoorhead(String doorhead) {
        this.doorhead = doorhead;
    }

    public String getEnabledate() {
        return enabledate;
    }

    public void setEnabledate(String enabledate) {
        this.enabledate = enabledate;
    }

    public String getFramenum() {
        return framenum;
    }

    public void setFramenum(String framenum) {
        this.framenum = framenum;
    }

    public String getIsenable() {
        return isenable;
    }

    public void setIsenable(String isenable) {
        this.isenable = isenable;
    }

    public String getKeydealer() {
        return keydealer;
    }

    public void setKeydealer(String keydealer) {
        this.keydealer = keydealer;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSaletype() {
        return saletype;
    }

    public void setSaletype(String saletype) {
        this.saletype = saletype;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
