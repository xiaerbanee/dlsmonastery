package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleInputDto {
    private String badProductIme;
    private String badProductName;
    private String badDepotName;
    private String badType;
    private String packageStatus;
    private String memory;
    private String fromDepotName;
    private String toDepotName;
    private LocalDate inputDate;
    private LocalDate replaceDate;
    private String replaceProductIme;
    private String replaceProductName;
    private BigDecimal replaceAmount;
    private String fleeShopName;
    private String contact;
    private String mobilePhone;
    private String address;
    private BigDecimal buyAmount;
    private String remarks;
    private Map<String,Object> extra= Maps.newHashMap();

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFleeShopName() {
        return fleeShopName;
    }

    public void setFleeShopName(String fleeShopName) {
        this.fleeShopName = fleeShopName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getBadProductIme() {
        return badProductIme;
    }

    public void setBadProductIme(String badProductIme) {
        this.badProductIme = badProductIme;
    }

    public String getBadProductName() {
        return badProductName;
    }

    public void setBadProductName(String badProductName) {
        this.badProductName = badProductName;
    }

    public String getBadDepotName() {
        return badDepotName;
    }

    public void setBadDepotName(String badDepotName) {
        this.badDepotName = badDepotName;
    }

    public String getBadType() {
        return badType;
    }

    public void setBadType(String badType) {
        this.badType = badType;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getFromDepotName() {
        return fromDepotName;
    }

    public void setFromDepotName(String fromDepotName) {
        this.fromDepotName = fromDepotName;
    }

    public String getToDepotName() {
        return toDepotName;
    }

    public void setToDepotName(String toDepotName) {
        this.toDepotName = toDepotName;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public LocalDate getReplaceDate() {
        return replaceDate;
    }

    public void setReplaceDate(LocalDate replaceDate) {
        this.replaceDate = replaceDate;
    }

    public String getReplaceProductIme() {
        return replaceProductIme;
    }

    public void setReplaceProductIme(String replaceProductIme) {
        this.replaceProductIme = replaceProductIme;
    }

    public String getReplaceProductName() {
        return replaceProductName;
    }

    public void setReplaceProductName(String replaceProductName) {
        this.replaceProductName = replaceProductName;
    }

    public BigDecimal getReplaceAmount() {
        return replaceAmount;
    }

    public void setReplaceAmount(BigDecimal replaceAmount) {
        this.replaceAmount = replaceAmount;
    }
}
