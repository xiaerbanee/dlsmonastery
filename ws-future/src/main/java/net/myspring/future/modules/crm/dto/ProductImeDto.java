package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeDto extends DataDto<ProductIme> {

    private String boxIme;
    private String ime;
    private String ime2;
    private String meid;
    private LocalDateTime retailDate;
    private LocalDateTime productImeUploadCreatedDate;
    private LocalDateTime productImeSaleCreatedDate;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "netType")
    private String productNetType;
    private String depotId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    private String inputType;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String productId;

    private String depotOfficeId;
    @CacheInput(inputKey = "offices",inputInstance = "depotOfficeId",outputInstance = "name")
    private String depotOfficeName;

    private String depotAreaName;
    private String depotAreaType;

    private String productImeSaleEmployeeId;
    @CacheInput(inputKey = "employees",inputInstance = "productImeSaleEmployeeId",outputInstance = "name")
    private String productImeSaleEmployeeName;

    private String productImeUploadEmployeeId;
    @CacheInput(inputKey = "employees",inputInstance = "productImeUploadEmployeeId",outputInstance = "name")
    private String productImeUploadEmployeeName;

    private String productTypeId;
    @CacheInput(inputKey = "productTypes",inputInstance = "productTypeId",outputInstance = "name")
    private String productTypeName;


    private LocalDateTime createdTime;
    private Boolean locked;
    private Boolean enabled;
    private String billId;

    @CacheInput(inputKey = "depots",inputInstance = "productImeSaleShopId",outputInstance = "name")
    private String productImeSaleShopName;
    private String productImeSaleShopId;
    private String productImeSaleCreatedBy;
    @CacheInput(inputKey = "accounts",inputInstance = "productImeSaleCreatedBy",outputInstance = "loginName")
    private String productImeSaleCreatedByName;
    private String productImeUploadShopId;
    @CacheInput(inputKey = "depots",inputInstance = "productImeUploadShopId",outputInstance = "name")
    private String productImeUploadShopName;
    private String productImeUploadCreatedBy;
    @CacheInput(inputKey = "accounts",inputInstance = "productImeUploadCreatedBy",outputInstance = "loginName")
    private String productImeUploadCreatedByName;

    public String getDepotOfficeId() {
        return depotOfficeId;
    }

    public void setDepotOfficeId(String depotOfficeId) {
        this.depotOfficeId = depotOfficeId;
    }

    public String getDepotOfficeName() {
        return depotOfficeName;
    }

    public void setDepotOfficeName(String depotOfficeName) {
        this.depotOfficeName = depotOfficeName;
    }

    public String getDepotAreaName() {
        return depotAreaName;
    }

    public void setDepotAreaName(String depotAreaName) {
        this.depotAreaName = depotAreaName;
    }

    public String getDepotAreaType() {
        return depotAreaType;
    }

    public void setDepotAreaType(String depotAreaType) {
        this.depotAreaType = depotAreaType;
    }

    public String getProductImeSaleEmployeeId() {
        return productImeSaleEmployeeId;
    }

    public void setProductImeSaleEmployeeId(String productImeSaleEmployeeId) {
        this.productImeSaleEmployeeId = productImeSaleEmployeeId;
    }

    public String getProductImeSaleEmployeeName() {
        return productImeSaleEmployeeName;
    }

    public void setProductImeSaleEmployeeName(String productImeSaleEmployeeName) {
        this.productImeSaleEmployeeName = productImeSaleEmployeeName;
    }

    public String getProductImeUploadEmployeeId() {
        return productImeUploadEmployeeId;
    }

    public void setProductImeUploadEmployeeId(String productImeUploadEmployeeId) {
        this.productImeUploadEmployeeId = productImeUploadEmployeeId;
    }

    public String getProductImeUploadEmployeeName() {
        return productImeUploadEmployeeName;
    }

    public void setProductImeUploadEmployeeName(String productImeUploadEmployeeName) {
        this.productImeUploadEmployeeName = productImeUploadEmployeeName;
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

    public String getBoxIme() {
        return boxIme;
    }

    public void setBoxIme(String boxIme) {
        this.boxIme = boxIme;
    }

    public String getProductImeSaleShopName() {
        return productImeSaleShopName;
    }

    public void setProductImeSaleShopName(String productImeSaleShopName) {
        this.productImeSaleShopName = productImeSaleShopName;
    }

    public String getProductImeSaleShopId() {
        return productImeSaleShopId;
    }

    public void setProductImeSaleShopId(String productImeSaleShopId) {
        this.productImeSaleShopId = productImeSaleShopId;
    }

    public String getProductImeSaleCreatedBy() {
        return productImeSaleCreatedBy;
    }

    public void setProductImeSaleCreatedBy(String productImeSaleCreatedBy) {
        this.productImeSaleCreatedBy = productImeSaleCreatedBy;
    }

    public String getProductImeSaleCreatedByName() {
        return productImeSaleCreatedByName;
    }

    public void setProductImeSaleCreatedByName(String productImeSaleCreatedByName) {
        this.productImeSaleCreatedByName = productImeSaleCreatedByName;
    }

    public String getProductImeUploadShopId() {
        return productImeUploadShopId;
    }

    public void setProductImeUploadShopId(String productImeUploadShopId) {
        this.productImeUploadShopId = productImeUploadShopId;
    }

    public String getProductImeUploadShopName() {
        return productImeUploadShopName;
    }

    public void setProductImeUploadShopName(String productImeUploadShopName) {
        this.productImeUploadShopName = productImeUploadShopName;
    }

    public String getProductImeUploadCreatedBy() {
        return productImeUploadCreatedBy;
    }

    public void setProductImeUploadCreatedBy(String productImeUploadCreatedBy) {
        this.productImeUploadCreatedBy = productImeUploadCreatedBy;
    }

    public String getProductImeUploadCreatedByName() {
        return productImeUploadCreatedByName;
    }

    public void setProductImeUploadCreatedByName(String productImeUploadCreatedByName) {
        this.productImeUploadCreatedByName = productImeUploadCreatedByName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getProductImeUploadCreatedDate() {
        return productImeUploadCreatedDate;
    }

    public void setProductImeUploadCreatedDate(LocalDateTime productImeUploadCreatedDate) {
        this.productImeUploadCreatedDate = productImeUploadCreatedDate;
    }

    public LocalDateTime getProductImeSaleCreatedDate() {
        return productImeSaleCreatedDate;
    }

    public void setProductImeSaleCreatedDate(LocalDateTime productImeSaleCreatedDate) {
        this.productImeSaleCreatedDate = productImeSaleCreatedDate;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getIme2() {
        return ime2;
    }

    public void setIme2(String ime2) {
        this.ime2 = ime2;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNetType() {
        return productNetType;
    }

    public void setProductNetType(String productNetType) {
        this.productNetType = productNetType;
    }
}
