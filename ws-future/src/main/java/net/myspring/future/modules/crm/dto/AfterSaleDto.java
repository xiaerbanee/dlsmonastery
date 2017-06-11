package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleDto extends DataDto<AfterSale> {
    private String type;
    private String badProductImeId;
    private String badProductId;
    private String badDepotId;
    private String badProductName;
    private String badDepotName;
    private String badType;
    private String packageStatus;
    private String memory;
    private String badProductIme;
    private String replaceProductName;
    private String fromDepotName;
    private String toDepotName;
    private String detailRemarks;
    private String ime;
    private String fleeShopName;
    private String contact;
    private String mobilePhone;
    private String address;
    private BigDecimal buyAmount;

    public String getDetailRemarks() {
        return detailRemarks;
    }

    public void setDetailRemarks(String detailRemarks) {
        this.detailRemarks = detailRemarks;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
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

    public String getToDepotName() {
        return toDepotName;
    }

    public void setToDepotName(String toDepotName) {
        this.toDepotName = toDepotName;
    }

    public String getReplaceProductName() {
        return replaceProductName;
    }

    public void setReplaceProductName(String replaceProductName) {
        this.replaceProductName = replaceProductName;
    }

    public String getFromDepotName() {
        return fromDepotName;
    }

    public void setFromDepotName(String fromDepotName) {
        this.fromDepotName = fromDepotName;
    }

    public String getBadProductImeId() {
        return badProductImeId;
    }

    public void setBadProductImeId(String badProductImeId) {
        this.badProductImeId = badProductImeId;
    }

    public String getBadProductId() {
        return badProductId;
    }

    public void setBadProductId(String badProductId) {
        this.badProductId = badProductId;
    }

    public String getBadDepotId() {
        return badDepotId;
    }

    public void setBadDepotId(String badDepotId) {
        this.badDepotId = badDepotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBadProductIme() {
        return badProductIme;
    }

    public void setBadProductIme(String badProductIme) {
        this.badProductIme = badProductIme;
    }
}
