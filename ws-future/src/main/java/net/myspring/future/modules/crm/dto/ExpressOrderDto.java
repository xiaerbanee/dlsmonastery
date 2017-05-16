package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpressOrderDto extends DataDto<ExpressOrder> {

    private String expressCompanyId;
    @CacheInput(inputKey = "expressCompanies",inputInstance = "expressCompanyId",outputInstance = "name")
    private String expressCompanyName;
    private String fromDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "fromDepotId",outputInstance = "name")
    private String fromDepotName;
    private String toDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "toDepotId",outputInstance = "name")
    private String toDepotName;

    private BigDecimal shouldGet;
    private BigDecimal shouldPay;
    private String address;
    private LocalDateTime expressPrintDate;
    private LocalDateTime outPrintDate;
    private LocalDateTime createDate;
    private LocalDate printDate;
    private String extendType;
    private String extendBusinessId;
    private String extendId;
    private String shipType;

    private BigDecimal realPay;
    private String contator;
    private String mobilePhone;
    private Integer expressPrintQty;

    private BigDecimal weight;
    private Integer totalQty;
    private BigDecimal averageWeight;

    private String expressCodes;

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getExpressCodes() {
        return expressCodes;
    }

    public void setExpressCodes(String expressCodes) {
        this.expressCodes = expressCodes;
    }

    public BigDecimal getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(BigDecimal averageWeight) {
        this.averageWeight = averageWeight;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
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

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public BigDecimal getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(BigDecimal shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getExpressPrintDate() {
        return expressPrintDate;
    }

    public void setExpressPrintDate(LocalDateTime expressPrintDate) {
        this.expressPrintDate = expressPrintDate;
    }

    public LocalDateTime getOutPrintDate() {
        return outPrintDate;
    }

    public void setOutPrintDate(LocalDateTime outPrintDate) {
        this.outPrintDate = outPrintDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDate getPrintDate() {
        return printDate;
    }

    public void setPrintDate(LocalDate printDate) {
        this.printDate = printDate;
    }

    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public String getExtendBusinessId() {
        return extendBusinessId;
    }

    public void setExtendBusinessId(String extendBusinessId) {
        this.extendBusinessId = extendBusinessId;
    }

    public BigDecimal getRealPay() {
        return realPay;
    }

    public void setRealPay(BigDecimal realPay) {
        this.realPay = realPay;
    }

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getExpressPrintQty() {
        return expressPrintQty;
    }

    public void setExpressPrintQty(Integer expressPrintQty) {
        this.expressPrintQty = expressPrintQty;
    }

}
