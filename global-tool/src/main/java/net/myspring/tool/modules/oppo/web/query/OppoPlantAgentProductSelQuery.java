package net.myspring.tool.modules.oppo.web.query;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lihx on 2017/4/19.
 */
public class OppoPlantAgentProductSelQuery {
    private String id;
    private String colorId;
    private String colorName;
    private String itemDesc;
    private Boolean itemNumber;
    private String productName;
    private String lxProductName;

    private List<String> ids= Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Boolean getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Boolean itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLxProductName() {
        return lxProductName;
    }

    public void setLxProductName(String lxProductName) {
        this.lxProductName = lxProductName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
