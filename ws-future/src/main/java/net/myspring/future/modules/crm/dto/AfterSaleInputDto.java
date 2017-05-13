package net.myspring.future.modules.crm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleInputDto {
    private String ime;
    private String productName;
    private String depotName;
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

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
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
