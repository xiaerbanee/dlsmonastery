package net.myspring.future.modules.crm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by wangzm on 2017/5/13.
 */
public class AfterSaleCompanyDto {
    private String badProductName;
    private String replaceProductName;
    private LocalDate inputDate;
    private String badIme;
    private String packageStatus;
    private String badType;
    private String memory;
    private String remarks;
    private BigDecimal replaceAmount;

    public BigDecimal getReplaceAmount() {
        return replaceAmount;
    }

    public void setReplaceAmount(BigDecimal replaceAmount) {
        this.replaceAmount = replaceAmount;
    }

    public String getBadProductName() {
        return badProductName;
    }

    public void setBadProductName(String badProductName) {
        this.badProductName = badProductName;
    }

    public String getReplaceProductName() {
        return replaceProductName;
    }

    public void setReplaceProductName(String replaceProductName) {
        this.replaceProductName = replaceProductName;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public String getBadIme() {
        return badIme;
    }

    public void setBadIme(String badIme) {
        this.badIme = badIme;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getBadType() {
        return badType;
    }

    public void setBadType(String badType) {
        this.badType = badType;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
