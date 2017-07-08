package net.myspring.future.modules.layout.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;


public class AdGoodsOrderDto extends DataDto<AdGoodsOrder>{
    private String storeId;
    @CacheInput(inputKey = "depots", inputInstance = "storeId", outputInstance = "name")
    private String storeName;
    private String outShopId;
    @CacheInput(inputKey = "depots", inputInstance = "outShopId", outputInstance = "name")
    private String outShopName;
    private String shopId;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "name")
    private String shopName;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "address")
    private String shopAddress;

    private String shopOfficeId;
    @CacheInput(inputKey = "offices", inputInstance = "shopOfficeId", outputInstance = "name")
    private String shopOfficeName;
    private String shopAreaId;
    @CacheInput(inputKey = "offices", inputInstance = "shopAreaId", outputInstance = "name")
    private String shopAreaName;
    private String depotShopAreaType;
    private BigDecimal amount;
    private String outCode;
    private String billType;
    private LocalDate billDate;
    private String billRemarks;

    private String processStatus;
    private String businessId;
    private String parentId;

    private String expressOrderExpressCodes;
    private String expressOrderContator;
    private String expressOrderAddress;
    private String expressOrderMobilePhone;
    private String expressOrderExpressCompanyId;
    @CacheInput(inputKey = "expressCompanies", inputInstance = "expressOrderExpressCompanyId", outputInstance = "name")
    private String expressOrderExpressCompanyName;
    private LocalDate expressOrderOutPrintDate;
    private String expressOrderId;
    private BigDecimal expressOrderShouldPay;
    private BigDecimal expressOrderShouldGet;
    private BigDecimal expressOrderRealPay;

    private String processPositionId;
    private String processInstanceId;
    private String investInCause;
    private String billAddress;
    private Boolean splitBill;

    private Integer smallQty;
    private Integer mediumQty;
    private Integer largeQty;

    private String employeeId;
    @CacheInput(inputKey = "employees", inputInstance = "employeeId", outputInstance = "name")
    private String employeeName;
    @CacheInput(inputKey = "employees", inputInstance = "employeeId", outputInstance = "mobilePhone")
    private String employeeMobilePhone;
    private Boolean locked;

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
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

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDepotShopAreaType() {
        return depotShopAreaType;
    }

    public void setDepotShopAreaType(String depotShopAreaType) {
        this.depotShopAreaType = depotShopAreaType;
    }

    public String getExpressOrderExpressCompanyName() {
        return expressOrderExpressCompanyName;
    }

    public void setExpressOrderExpressCompanyName(String expressOrderExpressCompanyName) {
        this.expressOrderExpressCompanyName = expressOrderExpressCompanyName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeMobilePhone() {
        return employeeMobilePhone;
    }

    public void setEmployeeMobilePhone(String employeeMobilePhone) {
        this.employeeMobilePhone = employeeMobilePhone;
    }

    public LocalDate getExpressOrderOutPrintDate() {
        return expressOrderOutPrintDate;
    }

    public void setExpressOrderOutPrintDate(LocalDate expressOrderOutPrintDate) {
        this.expressOrderOutPrintDate = expressOrderOutPrintDate;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public Integer getSmallQty() {
        return smallQty;
    }

    public void setSmallQty(Integer smallQty) {
        this.smallQty = smallQty;
    }

    public Integer getMediumQty() {
        return mediumQty;
    }

    public void setMediumQty(Integer mediumQty) {
        this.mediumQty = mediumQty;
    }

    public Integer getLargeQty() {
        return largeQty;
    }

    public void setLargeQty(Integer largeQty) {
        this.largeQty = largeQty;
    }

    public Boolean getSplitBill() {
        return splitBill;
    }

    public void setSplitBill(Boolean splitBill) {
        this.splitBill = splitBill;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getExpressOrderExpressCompanyId() {
        return expressOrderExpressCompanyId;
    }

    public void setExpressOrderExpressCompanyId(String expressOrderExpressCompanyId) {
        this.expressOrderExpressCompanyId = expressOrderExpressCompanyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getInvestInCause() {
        return investInCause;
    }

    public void setInvestInCause(String investInCause) {
        this.investInCause = investInCause;
    }

    public String getFormatId(){
        if(StringUtils.isBlank(parentId) || parentId.equals(getId())){
            return RequestUtils.getCompanyName() + StringUtils.trimToEmpty(getId());
        }
        return RequestUtils.getCompanyName() + StringUtils.trimToEmpty(parentId)+ CharConstant.UNDER_LINE + StringUtils.trimToEmpty(getId());
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOutShopId() {
        return outShopId;
    }

    public void setOutShopId(String outShopId) {
        this.outShopId = outShopId;
    }

    public String getOutShopName() {
        return outShopName;
    }

    public void setOutShopName(String outShopName) {
        this.outShopName = outShopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOfficeId() {
        return shopOfficeId;
    }

    public void setShopOfficeId(String shopOfficeId) {
        this.shopOfficeId = shopOfficeId;
    }

    public String getShopOfficeName() {
        return shopOfficeName;
    }

    public void setShopOfficeName(String shopOfficeName) {
        this.shopOfficeName = shopOfficeName;
    }

    public String getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(String shopAreaId) {
        this.shopAreaId = shopAreaId;
    }

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getExpressOrderExpressCodes() {
        return expressOrderExpressCodes;
    }

    public void setExpressOrderExpressCodes(String expressOrderExpressCodes) {
        this.expressOrderExpressCodes = expressOrderExpressCodes;
    }

    public String getProcessPositionId() {
        return processPositionId;
    }

    public void setProcessPositionId(String processPositionId) {
        this.processPositionId = processPositionId;
    }

    public Boolean getAuditable(){
        return RequestUtils.getPositionId().equals(getProcessPositionId()) || RequestUtils.getAccountId().equalsIgnoreCase("1");
    }

    public Boolean getEditable(){
        return RequestUtils.getAccountId().equals(getCreatedBy()) || RequestUtils.getAccountId().equalsIgnoreCase("1");
    }

    public boolean getPrint(){
        return expressOrderOutPrintDate != null;
    }
}
