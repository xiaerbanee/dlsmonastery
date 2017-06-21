package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class ProductImeSaleDto extends DataDto<ProductImeSale> {

    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String shopId;
    private String shopAreaId;
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;
    private String depotShopAreaType;
    @CacheInput(inputKey = "offices",inputInstance = "shopOfficeId",outputInstance = "name")
    private String shopOfficeName;
    private String shopOfficeId;
    private String productImeIme;
    private String productImeMeid;
    @CacheInput(inputKey = "products",inputInstance = "productImeProductId",outputInstance = "name")
    private String productImeProductName;
    private String productImeProductId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String employeeId;
    private Boolean isBack;

    private String buyer;
    private Integer buyerAge;
    private String buyerSex;
    private String buyerPhone;
    private String buyerSchool;
    private String buyerGrade;
    private String hongbao;
    private LocalDate lotteryDate;

    private String productImeProductImeUploadId;
    private String productImeId;
    private LocalDateTime retailDate;

    public String getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(String shopAreaId) {
        this.shopAreaId = shopAreaId;
    }

    public String getDepotShopAreaType() {
        return depotShopAreaType;
    }

    public void setDepotShopAreaType(String depotShopAreaType) {
        this.depotShopAreaType = depotShopAreaType;
    }

    public String getProductImeProductImeUploadId() {
        return productImeProductImeUploadId;
    }

    public void setProductImeProductImeUploadId(String productImeProductImeUploadId) {
        this.productImeProductImeUploadId = productImeProductImeUploadId;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getShopOfficeId() {
        return shopOfficeId;
    }

    public void setShopOfficeId(String shopOfficeId) {
        this.shopOfficeId = shopOfficeId;
    }

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public String getShopOfficeName() {
        return shopOfficeName;
    }

    public void setShopOfficeName(String shopOfficeName) {
        this.shopOfficeName = shopOfficeName;
    }

    public String getProductImeMeid() {
        return productImeMeid;
    }

    public void setProductImeMeid(String productImeMeid) {
        this.productImeMeid = productImeMeid;
    }

    public String getHongbao() {
        return hongbao;
    }

    public void setHongbao(String hongbao) {
        this.hongbao = hongbao;
    }

    public LocalDate getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(LocalDate lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Integer getBuyerAge() {
        return buyerAge;
    }

    public void setBuyerAge(Integer buyerAge) {
        this.buyerAge = buyerAge;
    }

    public String getBuyerSex() {
        return buyerSex;
    }

    public void setBuyerSex(String buyerSex) {
        this.buyerSex = buyerSex;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerSchool() {
        return buyerSchool;
    }

    public void setBuyerSchool(String buyerSchool) {
        this.buyerSchool = buyerSchool;
    }

    public String getBuyerGrade() {
        return buyerGrade;
    }

    public void setBuyerGrade(String buyerGrade) {
        this.buyerGrade = buyerGrade;
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

    public String getProductImeProductName() {
        return productImeProductName;
    }

    public void setProductImeProductName(String productImeProductName) {
        this.productImeProductName = productImeProductName;
    }

    public String getProductImeProductId() {
        return productImeProductId;
    }

    public void setProductImeProductId(String productImeProductId) {
        this.productImeProductId = productImeProductId;
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

    public Boolean getIsBack() {
        return isBack;
    }

    public void setIsBack(Boolean isBack) {
        this.isBack = isBack;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }
}
