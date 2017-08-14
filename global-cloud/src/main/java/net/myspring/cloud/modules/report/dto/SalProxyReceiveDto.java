package net.myspring.cloud.modules.report.dto;

import java.math.BigDecimal;

/**
 * 代理销售收款汇总报表
 */
public class SalProxyReceiveDto {
    private String groupNumber;
    private String groupName;
    private String flx;
    private BigDecimal fallAmount;
    private String bankNumber;
    private String bankName;
    private BigDecimal skAmount;
    //占比
    private BigDecimal backLV;

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFlx() {
        return flx;
    }

    public void setFlx(String flx) {
        this.flx = flx;
    }

    public BigDecimal getFallAmount() {
        return fallAmount;
    }

    public void setFallAmount(BigDecimal fallAmount) {
        this.fallAmount = fallAmount;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(BigDecimal skAmount) {
        this.skAmount = skAmount;
    }

    public BigDecimal getBackLV() {
        return backLV;
    }

    public void setBackLV(BigDecimal backLV) {
        this.backLV = backLV;
    }
}
