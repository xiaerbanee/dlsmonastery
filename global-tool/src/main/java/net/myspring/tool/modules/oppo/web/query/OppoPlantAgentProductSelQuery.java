package net.myspring.tool.modules.oppo.web.query;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lihx on 2017/4/19.
 */
public class OppoPlantAgentProductSelQuery {
    private String itemDesc;
    private String productName;
    private String itemNumberStr;
    private List<String> itemNumberList = Lists.newArrayList();
    private List<String> productNameList = Lists.newArrayList();

    public List<String> getProductNameList() {
        return productNameList;
    }

    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
