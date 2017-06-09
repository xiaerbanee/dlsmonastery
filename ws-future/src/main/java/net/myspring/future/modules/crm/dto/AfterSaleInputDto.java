package net.myspring.future.modules.crm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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
