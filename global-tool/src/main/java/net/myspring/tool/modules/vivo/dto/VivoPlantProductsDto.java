package net.myspring.tool.modules.vivo.dto;

import net.myspring.util.cahe.annotation.CacheInput;

public class VivoPlantProductsDto {
    private String id;
    private String typeName;
    private String colorName;
    private String itemNumber;
    private String itemDesc;
    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String lxProductId;
    @CacheInput(inputKey = "products",inputInstance = "lxProductId",outputInstance = "name")
    private String lxProductName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLxProductId() {
        return lxProductId;
    }

    public void setLxProductId(String lxProductId) {
        this.lxProductId = lxProductId;
    }

    public String getLxProductName() {
        return lxProductName;
    }

    public void setLxProductName(String lxProductName) {
        this.lxProductName = lxProductName;
    }
}
