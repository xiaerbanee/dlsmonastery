package net.myspring.cloud.modules.report.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2016/12/19.
 */
public class ReceivableForSummaryDto {
    private String customerId;
    private String customerName;
    private String primaryGroup;
    private String primaryGroupName;
    private BigDecimal beginAmount;
    private BigDecimal receivableAmount;
    private BigDecimal actualReceivable;
    private BigDecimal endAmount;

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

    public String getPrimaryGroup() {
        return primaryGroup;
    }

    public void setPrimaryGroup(String primaryGroup) {
        this.primaryGroup = primaryGroup;
    }

    public String getPrimaryGroupName() {
        return primaryGroupName;
    }

    public void setPrimaryGroupName(String primaryGroupName) {
        this.primaryGroupName = primaryGroupName;
    }

    public BigDecimal getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(BigDecimal beginAmount) {
        this.beginAmount = beginAmount;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public BigDecimal getActualReceivable() {
        return actualReceivable;
    }

    public void setActualReceivable(BigDecimal actualReceivable) {
        this.actualReceivable = actualReceivable;
    }

    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }
}
