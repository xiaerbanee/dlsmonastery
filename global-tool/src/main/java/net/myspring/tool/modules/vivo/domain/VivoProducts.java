package net.myspring.tool.modules.vivo.domain;


import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="vivo_products")
public class VivoProducts extends IdEntity<VivoProducts> {
    private String typeId;
    private String colorId;
    private String colorName;
    private String typeName;
    private String brandName;
    private String remark;
    private String shortCut;
    private String codeRecordType;
    private String shortCutColor;
    private String unit;
    private String brandType;

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

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public String getCodeRecordType() {
        return codeRecordType;
    }

    public void setCodeRecordType(String codeRecordType) {
        this.codeRecordType = codeRecordType;
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
}
