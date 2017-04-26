package net.myspring.cloud.modules.report.dto;

import java.math.BigDecimal;

/**
 * 委托代销报表
 * Created by liuj on 2016-09-26.
 */
public class ConsignmentForShowDto {
    private String customerCode;
    private String customerName;
    private String goodsCode;
    private String goodsName;
    private BigDecimal consignmentInitialQuantity;
    private BigDecimal consignmentInitialPrice;
    private BigDecimal consignmentInitialAmount;
    private BigDecimal consignmentSendQuantity;
    private BigDecimal consignmentSendPrice;
    private BigDecimal consignmentSendAmount;
    private BigDecimal consignmentSettlementQuantity;
    private BigDecimal consignmentSettlementPrice;
    private BigDecimal consignmentSettlementAmount;
    private BigDecimal consignmentNotSettledQuantity;
    private BigDecimal consignmentNotSettledPrice;
    private BigDecimal consignmentNotSettledAmount;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getConsignmentInitialQuantity() {
        return consignmentInitialQuantity;
    }

    public void setConsignmentInitialQuantity(BigDecimal consignmentInitialQuantity) {
        this.consignmentInitialQuantity = consignmentInitialQuantity;
    }

    public BigDecimal getConsignmentInitialPrice() {
        return consignmentInitialPrice;
    }

    public void setConsignmentInitialPrice(BigDecimal consignmentInitialPrice) {
        this.consignmentInitialPrice = consignmentInitialPrice;
    }

    public BigDecimal getConsignmentInitialAmount() {
        return consignmentInitialAmount;
    }

    public void setConsignmentInitialAmount(BigDecimal consignmentInitialAmount) {
        this.consignmentInitialAmount = consignmentInitialAmount;
    }

    public BigDecimal getConsignmentSendQuantity() {
        return consignmentSendQuantity;
    }

    public void setConsignmentSendQuantity(BigDecimal consignmentSendQuantity) {
        this.consignmentSendQuantity = consignmentSendQuantity;
    }

    public BigDecimal getConsignmentSendPrice() {
        return consignmentSendPrice;
    }

    public void setConsignmentSendPrice(BigDecimal consignmentSendPrice) {
        this.consignmentSendPrice = consignmentSendPrice;
    }

    public BigDecimal getConsignmentSendAmount() {
        return consignmentSendAmount;
    }

    public void setConsignmentSendAmount(BigDecimal consignmentSendAmount) {
        this.consignmentSendAmount = consignmentSendAmount;
    }

    public BigDecimal getConsignmentSettlementQuantity() {
        return consignmentSettlementQuantity;
    }

    public void setConsignmentSettlementQuantity(BigDecimal consignmentSettlementQuantity) {
        this.consignmentSettlementQuantity = consignmentSettlementQuantity;
    }

    public BigDecimal getConsignmentSettlementPrice() {
        return consignmentSettlementPrice;
    }

    public void setConsignmentSettlementPrice(BigDecimal consignmentSettlementPrice) {
        this.consignmentSettlementPrice = consignmentSettlementPrice;
    }

    public BigDecimal getConsignmentSettlementAmount() {
        return consignmentSettlementAmount;
    }

    public void setConsignmentSettlementAmount(BigDecimal consignmentSettlementAmount) {
        this.consignmentSettlementAmount = consignmentSettlementAmount;
    }

    public BigDecimal getConsignmentNotSettledQuantity() {
        return consignmentNotSettledQuantity;
    }

    public void setConsignmentNotSettledQuantity(BigDecimal consignmentNotSettledQuantity) {
        this.consignmentNotSettledQuantity = consignmentNotSettledQuantity;
    }

    public BigDecimal getConsignmentNotSettledPrice() {
        return consignmentNotSettledPrice;
    }

    public void setConsignmentNotSettledPrice(BigDecimal consignmentNotSettledPrice) {
        this.consignmentNotSettledPrice = consignmentNotSettledPrice;
    }

    public BigDecimal getConsignmentNotSettledAmount() {
        return consignmentNotSettledAmount;
    }

    public void setConsignmentNotSettledAmount(BigDecimal consignmentNotSettledAmount) {
        this.consignmentNotSettledAmount = consignmentNotSettledAmount;
    }
}
