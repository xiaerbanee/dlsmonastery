package net.myspring.future.modules.layout.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.IdDto;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;


public class AdGoodsOrderDetailExportDto extends IdDto<AdGoodsOrderDetail> {

    private String adGoodsOrderOutShopId;
    @CacheInput(inputKey = "depots", inputInstance = "adGoodsOrderOutShopId", outputInstance = "name")
    private String adGoodsOrderOutShopName;
    private String adGoodsOrderShopId;
    @CacheInput(inputKey = "depots", inputInstance = "adGoodsOrderShopId", outputInstance = "name")
    private String adGoodsOrderShopName;
    @CacheInput(inputKey = "depots", inputInstance = "adGoodsOrderShopId", outputInstance = "address")
    private String adGoodsOrderShopAddress;
    private String adGoodsOrderOfficeId;
    @CacheInput(inputKey = "offices", inputInstance = "adGoodsOrderOfficeId", outputInstance = "name")
    private String adGoodsOrderOfficeName;

    private String adGoodsOrderBillRemarks;

    private String adGoodsOrderCreatedBy;
    @CacheInput(inputKey = "accounts",inputInstance = "adGoodsOrderCreatedBy",outputInstance = "loginName")
    private String adGoodsOrderCreatedByName;
    private LocalDate adGoodsOrderCreatedDate;

    private String adGoodsOrderParentId;
    private String adGoodsOrderId;
    private String adGoodsOrderShopAreaId;
    @CacheInput(inputKey = "offices", inputInstance = "adGoodsOrderShopAreaId", outputInstance = "name")
    private String adGoodsOrderShopAreaName;
    private String adGoodsOrderDepotShopAreaType;

    private String adGoodsOrderOutCode;
    private LocalDate adGoodsOrderBillDate;
    private String adGoodsOrderProcessStatus;

    private String expressOrderExpressCodes;
    private String expressOrderContator;
    private String expressOrderAddress;
    private String expressOrderMobilePhone;
    private String expressOrderExpressCompanyId;
    @CacheInput(inputKey = "expressCompanies", inputInstance = "expressOrderExpressCompanyId", outputInstance = "name")
    private String expressOrderExpressCompanyName;
    private LocalDate expressOrderOutPrintDate;
    private BigDecimal expressOrderShouldPay;
    private BigDecimal expressOrderShouldGet;
    private BigDecimal expressOrderRealPay;

    private String adGoodsOrderBillAddress;

    private String adGoodsOrderEmployeeId;
    @CacheInput(inputKey = "employees", inputInstance = "adGoodsOrderEmployeeId", outputInstance = "name")
    private String adGoodsOrderEmployeeName;
    @CacheInput(inputKey = "employees", inputInstance = "adGoodsOrderEmployeeId", outputInstance = "mobilePhone")
    private String adGoodsOrderEmployeeMobilePhone;

    private String productName;
    private String productCode;
    private BigDecimal productVolume;
    private BigDecimal productPrice2;
    private Integer qty;
    private Integer confirmQty;
    private Integer billQty;
    private Integer shippedQty;

    public String getAdGoodsOrderFormatId(){
        if(StringUtils.isBlank(adGoodsOrderParentId) || adGoodsOrderParentId.equals(adGoodsOrderId)){
            return RequestUtils.getCompanyName() + StringUtils.trimToEmpty(adGoodsOrderId);
        }
        return RequestUtils.getCompanyName() + StringUtils.trimToEmpty(adGoodsOrderParentId)+CharConstant.UNDER_LINE + StringUtils.trimToEmpty(adGoodsOrderId);
    }

    public String getAdGoodsOrderBillRemarks() {
        return adGoodsOrderBillRemarks;
    }

    public void setAdGoodsOrderBillRemarks(String adGoodsOrderBillRemarks) {
        this.adGoodsOrderBillRemarks = adGoodsOrderBillRemarks;
    }

    public String getAdGoodsOrderId() {
        return adGoodsOrderId;
    }

    public void setAdGoodsOrderId(String adGoodsOrderId) {
        this.adGoodsOrderId = adGoodsOrderId;
    }

    public String getAdGoodsOrderParentId() {
        return adGoodsOrderParentId;
    }

    public void setAdGoodsOrderParentId(String adGoodsOrderParentId) {
        this.adGoodsOrderParentId = adGoodsOrderParentId;
    }

    public BigDecimal getProductVolume() {
        return productVolume;
    }

    public void setProductVolume(BigDecimal productVolume) {
        this.productVolume = productVolume;
    }

    public BigDecimal getAmount(){
        if(billQty == null || productPrice2 == null){
            return null;
        }

        return new BigDecimal(billQty).multiply(productPrice2);
    }

    public String getAdGoodsOrderOutShopId() {
        return adGoodsOrderOutShopId;
    }

    public void setAdGoodsOrderOutShopId(String adGoodsOrderOutShopId) {
        this.adGoodsOrderOutShopId = adGoodsOrderOutShopId;
    }

    public String getAdGoodsOrderOutShopName() {
        return adGoodsOrderOutShopName;
    }

    public void setAdGoodsOrderOutShopName(String adGoodsOrderOutShopName) {
        this.adGoodsOrderOutShopName = adGoodsOrderOutShopName;
    }

    public String getAdGoodsOrderShopId() {
        return adGoodsOrderShopId;
    }

    public void setAdGoodsOrderShopId(String adGoodsOrderShopId) {
        this.adGoodsOrderShopId = adGoodsOrderShopId;
    }

    public String getAdGoodsOrderShopName() {
        return adGoodsOrderShopName;
    }

    public void setAdGoodsOrderShopName(String adGoodsOrderShopName) {
        this.adGoodsOrderShopName = adGoodsOrderShopName;
    }

    public String getAdGoodsOrderOfficeId() {
        return adGoodsOrderOfficeId;
    }

    public void setAdGoodsOrderOfficeId(String adGoodsOrderOfficeId) {
        this.adGoodsOrderOfficeId = adGoodsOrderOfficeId;
    }

    public String getAdGoodsOrderOfficeName() {
        return adGoodsOrderOfficeName;
    }

    public void setAdGoodsOrderOfficeName(String adGoodsOrderOfficeName) {
        this.adGoodsOrderOfficeName = adGoodsOrderOfficeName;
    }

    public String getAdGoodsOrderShopAddress() {
        return adGoodsOrderShopAddress;
    }

    public void setAdGoodsOrderShopAddress(String adGoodsOrderShopAddress) {
        this.adGoodsOrderShopAddress = adGoodsOrderShopAddress;
    }

    public String getAdGoodsOrderCreatedBy() {
        return adGoodsOrderCreatedBy;
    }

    public void setAdGoodsOrderCreatedBy(String adGoodsOrderCreatedBy) {
        this.adGoodsOrderCreatedBy = adGoodsOrderCreatedBy;
    }

    public String getAdGoodsOrderCreatedByName() {
        return adGoodsOrderCreatedByName;
    }

    public void setAdGoodsOrderCreatedByName(String adGoodsOrderCreatedByName) {
        this.adGoodsOrderCreatedByName = adGoodsOrderCreatedByName;
    }

    public LocalDate getAdGoodsOrderCreatedDate() {
        return adGoodsOrderCreatedDate;
    }

    public void setAdGoodsOrderCreatedDate(LocalDate adGoodsOrderCreatedDate) {
        this.adGoodsOrderCreatedDate = adGoodsOrderCreatedDate;
    }

    public String getAdGoodsOrderShopAreaId() {
        return adGoodsOrderShopAreaId;
    }

    public void setAdGoodsOrderShopAreaId(String adGoodsOrderShopAreaId) {
        this.adGoodsOrderShopAreaId = adGoodsOrderShopAreaId;
    }

    public String getAdGoodsOrderShopAreaName() {
        return adGoodsOrderShopAreaName;
    }

    public void setAdGoodsOrderShopAreaName(String adGoodsOrderShopAreaName) {
        this.adGoodsOrderShopAreaName = adGoodsOrderShopAreaName;
    }

    public String getAdGoodsOrderDepotShopAreaType() {
        return adGoodsOrderDepotShopAreaType;
    }

    public void setAdGoodsOrderDepotShopAreaType(String adGoodsOrderDepotShopAreaType) {
        this.adGoodsOrderDepotShopAreaType = adGoodsOrderDepotShopAreaType;
    }

    public String getAdGoodsOrderOutCode() {
        return adGoodsOrderOutCode;
    }

    public void setAdGoodsOrderOutCode(String adGoodsOrderOutCode) {
        this.adGoodsOrderOutCode = adGoodsOrderOutCode;
    }

    public LocalDate getAdGoodsOrderBillDate() {
        return adGoodsOrderBillDate;
    }

    public void setAdGoodsOrderBillDate(LocalDate adGoodsOrderBillDate) {
        this.adGoodsOrderBillDate = adGoodsOrderBillDate;
    }

    public String getAdGoodsOrderProcessStatus() {
        return adGoodsOrderProcessStatus;
    }

    public void setAdGoodsOrderProcessStatus(String adGoodsOrderProcessStatus) {
        this.adGoodsOrderProcessStatus = adGoodsOrderProcessStatus;
    }

    public String getExpressOrderExpressCodes() {
        return expressOrderExpressCodes;
    }

    public void setExpressOrderExpressCodes(String expressOrderExpressCodes) {
        this.expressOrderExpressCodes = expressOrderExpressCodes;
    }

    public String getExpressOrderContator() {
        return expressOrderContator;
    }

    public void setExpressOrderContator(String expressOrderContator) {
        this.expressOrderContator = expressOrderContator;
    }

    public String getExpressOrderAddress() {
        return expressOrderAddress;
    }

    public void setExpressOrderAddress(String expressOrderAddress) {
        this.expressOrderAddress = expressOrderAddress;
    }

    public String getExpressOrderMobilePhone() {
        return expressOrderMobilePhone;
    }

    public void setExpressOrderMobilePhone(String expressOrderMobilePhone) {
        this.expressOrderMobilePhone = expressOrderMobilePhone;
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

    public LocalDate getExpressOrderOutPrintDate() {
        return expressOrderOutPrintDate;
    }

    public void setExpressOrderOutPrintDate(LocalDate expressOrderOutPrintDate) {
        this.expressOrderOutPrintDate = expressOrderOutPrintDate;
    }

    public BigDecimal getExpressOrderShouldPay() {
        return expressOrderShouldPay;
    }

    public void setExpressOrderShouldPay(BigDecimal expressOrderShouldPay) {
        this.expressOrderShouldPay = expressOrderShouldPay;
    }

    public BigDecimal getExpressOrderShouldGet() {
        return expressOrderShouldGet;
    }

    public void setExpressOrderShouldGet(BigDecimal expressOrderShouldGet) {
        this.expressOrderShouldGet = expressOrderShouldGet;
    }

    public BigDecimal getExpressOrderRealPay() {
        return expressOrderRealPay;
    }

    public void setExpressOrderRealPay(BigDecimal expressOrderRealPay) {
        this.expressOrderRealPay = expressOrderRealPay;
    }

    public String getAdGoodsOrderBillAddress() {
        return adGoodsOrderBillAddress;
    }

    public void setAdGoodsOrderBillAddress(String adGoodsOrderBillAddress) {
        this.adGoodsOrderBillAddress = adGoodsOrderBillAddress;
    }

    public String getAdGoodsOrderEmployeeId() {
        return adGoodsOrderEmployeeId;
    }

    public void setAdGoodsOrderEmployeeId(String adGoodsOrderEmployeeId) {
        this.adGoodsOrderEmployeeId = adGoodsOrderEmployeeId;
    }

    public String getAdGoodsOrderEmployeeName() {
        return adGoodsOrderEmployeeName;
    }

    public void setAdGoodsOrderEmployeeName(String adGoodsOrderEmployeeName) {
        this.adGoodsOrderEmployeeName = adGoodsOrderEmployeeName;
    }

    public String getAdGoodsOrderEmployeeMobilePhone() {
        return adGoodsOrderEmployeeMobilePhone;
    }

    public void setAdGoodsOrderEmployeeMobilePhone(String adGoodsOrderEmployeeMobilePhone) {
        this.adGoodsOrderEmployeeMobilePhone = adGoodsOrderEmployeeMobilePhone;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductPrice2() {
        return productPrice2;
    }

    public void setProductPrice2(BigDecimal productPrice2) {
        this.productPrice2 = productPrice2;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }
}
