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
    private String accountId;
    @CacheInput(inputKey = "accounts",inputInstance = "accountId",outputInstance = "positionId")
    private String positionId;
    @CacheInput(inputKey = "positions",inputInstance = "positionId",outputInstance = "name")
    private String positionName;
    private String saleShopId;
    private String goodsOrderShopId;
    private String accountShopIds;
    @CacheInput(inputKey = "depots",inputInstance = "saleShopId",outputInstance = "name")
    private String saleShopName;
    @CacheInput(inputKey = "depots",inputInstance = "goodsOrderShopId",outputInstance = "name")
    private String goodsOrderShopName;
    @CacheInput(inputKey = "depots",inputInstance = "accountShopIds",outputInstance = "name")
    private String accountShopNames;

    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "salerName")
    private String saleName;

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
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

    public String getAccountShopNames() {
        return accountShopNames;
    }

    public void setAccountShopNames(String accountShopNames) {
        this.accountShopNames = accountShopNames;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }
}
