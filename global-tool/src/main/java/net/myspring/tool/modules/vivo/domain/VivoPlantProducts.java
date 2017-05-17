package net.myspring.tool.modules.vivo.domain;


import net.myspring.common.domain.IdEntity;
import net.myspring.tool.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="vivo_plant_products")
public class VivoPlantProducts extends IdEntity<VivoPlantProducts> {
    private String typeId;
    private String colorId;
    private String colorName;
    private String typeName;
    private String brandId;
    private String brandName;
    private String remark;
    private String remarkColor;
    private String shortCut;
    private String shortCutColor;
    private String unit;
    private String brandType;
    private String itemDesc;
    private String itemNumber;
    private String itemType;
    private String orgId;
    private String codeRecordType;
    private String productId;
    private String lxProductId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkColor() {
        return remarkColor;
    }

    public void setRemarkColor(String remarkColor) {
        this.remarkColor = remarkColor;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public String getShortCutColor() {
        return shortCutColor;
    }

    public void setShortCutColor(String shortCutColor) {
        this.shortCutColor = shortCutColor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCodeRecordType() {
        return codeRecordType;
    }

    public void setCodeRecordType(String codeRecordType) {
        this.codeRecordType = codeRecordType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLxProductId() {
        return lxProductId;
    }

    public void setLxProductId(String lxProductId) {
        this.lxProductId = lxProductId;
    }
}
