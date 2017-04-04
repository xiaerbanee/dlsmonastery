package net.myspring.cloud.domain;

import java.math.BigDecimal;

/**
 * Created by lihx on 2016/12/19.
 */
public class ReceivableReportForSummary {
    private String customerId;
    private String customerName;
    private BigDecimal beginAmount;
    private BigDecimal payableAmount;
    private BigDecimal actualPayAmount;
    private BigDecimal endAmount;

    public ReceivableReportForSummary() {

    }

    public ReceivableReportForSummary(String customerId, String customerName, BigDecimal beginAmount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.beginAmount = beginAmount;
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

    public BigDecimal getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(BigDecimal beginAmount) {
        this.beginAmount = beginAmount;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public BigDecimal getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(BigDecimal actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }
}
