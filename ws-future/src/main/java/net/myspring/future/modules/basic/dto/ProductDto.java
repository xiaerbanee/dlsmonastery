package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/15.
 */
public class ProductDto extends DataDto<Product> {
    private String name;
    private String code;
    private String netType;
    private String outId;
    private String outGroupId;
    private String outGroupName;
    private String productTypeId;
    @CacheInput(inputKey = "productTypes",inputInstance = "productTypeId",outputInstance = "name")
    private String productTypeName;
    private Boolean visible;
    private BigDecimal volume;
    private Boolean hasIme;
    private Boolean allowOrder;
    private Boolean allowBill;
    private String image;
    private Boolean locked;

    private String expiryDateRemarks;
    private BigDecimal price2;
    private BigDecimal retailPrice;
    private BigDecimal depositPrice;
    private String mappingName;


    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(BigDecimal depositPrice) {
        this.depositPrice = depositPrice;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getExpiryDateRemarks() {
        return expiryDateRemarks;
    }

    public void setExpiryDateRemarks(String expiryDateRemarks) {
        this.expiryDateRemarks = expiryDateRemarks;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOutGroupId() {
        return outGroupId;
    }

    public void setOutGroupId(String outGroupId) {
        this.outGroupId = outGroupId;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
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

    public Boolean getHasIme() {
        return hasIme;
    }

    public void setHasIme(Boolean hasIme) {
        this.hasIme = hasIme;
    }

    public Boolean getAllowOrder() {
        return allowOrder;
    }

    public void setAllowOrder(Boolean allowOrder) {
        this.allowOrder = allowOrder;
    }

    public Boolean getAllowBill() {
        return allowBill;
    }

    public void setAllowBill(Boolean allowBill) {
        this.allowBill = allowBill;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}