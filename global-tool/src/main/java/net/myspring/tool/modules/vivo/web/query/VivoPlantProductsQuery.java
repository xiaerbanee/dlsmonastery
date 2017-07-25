package net.myspring.tool.modules.vivo.web.query;

import com.google.common.collect.Lists;

import java.util.List;

public class VivoPlantProductsQuery {
    private String colorName;
    private String itemDesc;
    private String typeName;
    private String itemNumberStr;
    private List<String> itemNumberList = Lists.newArrayList();

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getItemNumberStr() {
        return itemNumberStr;
    }

    public void setItemNumberStr(String itemNumberStr) {
        this.itemNumberStr = itemNumberStr;
    }

    public List<String> getItemNumberList() {
        return itemNumberList;
    }

    public void setItemNumberList(List<String> itemNumberList) {
        this.itemNumberList = itemNumberList;
    }
}
