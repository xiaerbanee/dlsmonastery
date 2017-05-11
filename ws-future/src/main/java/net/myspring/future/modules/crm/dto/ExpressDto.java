package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

public class ExpressDto extends DataDto<Express> {


    private String code;

    private String expressOrderExpressCompanyId;
    @CacheInput(inputKey = "expressCompanies",inputInstance = "expressOrderExpressCompanyId",outputInstance = "name")
    private String expressOrderExpressCompanyName;
    private String expressOrderExtendType;

    private String expressOrderExtendBusinessId;

    private String expressOrderFromDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "expressOrderFromDepotId",outputInstance = "name")
    private String expressOrderFromDepotName;

    private String expressOrderToDepotId;
    @CacheInput(inputKey = "depots",inputInstance = "expressOrderToDepotId",outputInstance = "name")
    private String expressOrderToDepotName;
    private String expressOrderContator;
    private String expressOrderMobilePhone;
    private String expressOrderAddress;
    private BigDecimal weight;
    private Integer qty;
    private BigDecimal shouldPay;
    private BigDecimal realPay;

    private String tracking;
    private String expressOrderId;

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpressOrderExpressCompanyId() {
        return expressOrderExpressCompanyId;
    }

    public void setExpressOrderExpressCompanyId(String expressOrderExpressCompanyId) {
        this.expressOrderExpressCompanyId = expressOrderExpressCompanyId;
    }

    public String getExpressOrderExpressCompanyName() {
        return expressOrderExpressCompanyName;
    }

    public void setExpressOrderExpressCompanyName(String expressOrderExpressCompanyName) {
        this.expressOrderExpressCompanyName = expressOrderExpressCompanyName;
    }

    public String getExpressOrderExtendType() {
        return expressOrderExtendType;
    }

    public void setExpressOrderExtendType(String expressOrderExtendType) {
        this.expressOrderExtendType = expressOrderExtendType;
    }

    public String getExpressOrderExtendBusinessId() {
        return expressOrderExtendBusinessId;
    }

    public void setExpressOrderExtendBusinessId(String expressOrderExtendBusinessId) {
        this.expressOrderExtendBusinessId = expressOrderExtendBusinessId;
    }

    public String getExpressOrderFromDepotId() {
        return expressOrderFromDepotId;
    }

    public void setExpressOrderFromDepotId(String expressOrderFromDepotId) {
        this.expressOrderFromDepotId = expressOrderFromDepotId;
    }

    public String getExpressOrderFromDepotName() {
        return expressOrderFromDepotName;
    }

    public void setExpressOrderFromDepotName(String expressOrderFromDepotName) {
        this.expressOrderFromDepotName = expressOrderFromDepotName;
    }

    public String getExpressOrderToDepotId() {
        return expressOrderToDepotId;
    }

    public void setExpressOrderToDepotId(String expressOrderToDepotId) {
        this.expressOrderToDepotId = expressOrderToDepotId;
    }

    public String getExpressOrderToDepotName() {
        return expressOrderToDepotName;
    }

    public void setExpressOrderToDepotName(String expressOrderToDepotName) {
        this.expressOrderToDepotName = expressOrderToDepotName;
    }

    public String getExpressOrderContator() {
        return expressOrderContator;
    }

    public void setExpressOrderContator(String expressOrderContator) {
        this.expressOrderContator = expressOrderContator;
    }

    public String getExpressOrderMobilePhone() {
        return expressOrderMobilePhone;
    }

    public void setExpressOrderMobilePhone(String expressOrderMobilePhone) {
        this.expressOrderMobilePhone = expressOrderMobilePhone;
    }

    public String getExpressOrderAddress() {
        return expressOrderAddress;
    }

    public void setExpressOrderAddress(String expressOrderAddress) {
        this.expressOrderAddress = expressOrderAddress;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(BigDecimal shouldPay) {
        this.shouldPay = shouldPay;
    }

    public BigDecimal getRealPay() {
        return realPay;
    }

    public void setRealPay(BigDecimal realPay) {
        this.realPay = realPay;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }




}
