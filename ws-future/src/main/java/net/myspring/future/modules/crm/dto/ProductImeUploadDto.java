package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.util.cahe.annotation.CacheInput;


public class ProductImeUploadDto extends DataDto<ProductImeUpload> {

    private String month;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String shopId;
    private String shopOfficeId;
    @CacheInput(inputKey = "offices",inputInstance = "shopOfficeId",outputInstance = "name")
    private String shopOfficeName;
    private String shopAreaId;
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;
    private String productImeProductId;
    @CacheInput(inputKey = "products",inputInstance = "productImeProductId",outputInstance = "name")
    private String productImeProductName;
    private String productTypeId;
    @CacheInput(inputKey = "productTypes",inputInstance = "productTypeId",outputInstance = "name")
    private String productTypeName;

    private String productImeIme;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String employeeId;
    private String status;
    private Boolean enabled;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "accountId")
    private String employeeAccountId;
    @CacheInput(inputKey = "accounts",inputInstance = "employeeAccountId",outputInstance = "positionId")
    private String employeePositionId;
    @CacheInput(inputKey = "positions",inputInstance = "employeePositionId",outputInstance = "name")
    private String employeePositionName;
    private String saleShopId;
    private String goodsOrderShopId;
    private String accountShopIds;
    @CacheInput(inputKey = "depots",inputInstance = "saleShopId",outputInstance = "name")
    private String saleShopName;
    @CacheInput(inputKey = "depots",inputInstance = "goodsOrderShopId",outputInstance = "name")
    private String goodsOrderShopName;


    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "salerName")
    private String employeeSalerName;

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

    public String getProductImeProductId() {
        return productImeProductId;
    }

    public void setProductImeProductId(String productImeProductId) {
        this.productImeProductId = productImeProductId;
    }

    public String getProductImeProductName() {
        return productImeProductName;
    }

    public void setProductImeProductName(String productImeProductName) {
        this.productImeProductName = productImeProductName;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductImeIme() {
        return productImeIme;
    }

    public void setProductImeIme(String productImeIme) {
        this.productImeIme = productImeIme;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmployeeAccountId() {
        return employeeAccountId;
    }

    public void setEmployeeAccountId(String employeeAccountId) {
        this.employeeAccountId = employeeAccountId;
    }

    public String getEmployeePositionId() {
        return employeePositionId;
    }

    public void setEmployeePositionId(String employeePositionId) {
        this.employeePositionId = employeePositionId;
    }

    public String getEmployeePositionName() {
        return employeePositionName;
    }

    public void setEmployeePositionName(String employeePositionName) {
        this.employeePositionName = employeePositionName;
    }

    public String getEmployeeSalerName() {
        return employeeSalerName;
    }

    public void setEmployeeSalerName(String employeeSalerName) {
        this.employeeSalerName = employeeSalerName;
    }

    public String getSaleShopId() {
        return saleShopId;
    }

    public void setSaleShopId(String saleShopId) {
        this.saleShopId = saleShopId;
    }

    public String getGoodsOrderShopId() {
        return goodsOrderShopId;
    }

    public void setGoodsOrderShopId(String goodsOrderShopId) {
        this.goodsOrderShopId = goodsOrderShopId;
    }

    public String getAccountShopIds() {
        return accountShopIds;
    }

    public void setAccountShopIds(String accountShopIds) {
        this.accountShopIds = accountShopIds;
    }

    public String getSaleShopName() {
        return saleShopName;
    }

    public void setSaleShopName(String saleShopName) {
        this.saleShopName = saleShopName;
    }

    public String getGoodsOrderShopName() {
        return goodsOrderShopName;
    }

    public void setGoodsOrderShopName(String goodsOrderShopName) {
        this.goodsOrderShopName = goodsOrderShopName;
    }

}
