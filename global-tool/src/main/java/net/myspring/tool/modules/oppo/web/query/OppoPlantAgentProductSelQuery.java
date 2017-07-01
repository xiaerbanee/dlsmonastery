package net.myspring.tool.modules.oppo.web.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.tool.common.query.BaseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/19.
 */
public class OppoPlantAgentProductSelQuery extends BaseQuery {
    private String itemDesc;
    private String productName;
    private String itemNumberStr;
    private List<String> itemNumberList = Lists.newArrayList();
    private Boolean lx;

    public Boolean getLx() {
        return lx;
    }

    public void setLx(Boolean lx) {
        this.lx = lx;
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
