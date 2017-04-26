package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lihx on 2017/4/19.
 */
public class ShopAdTypeQuery {
    private String totalPriceType;
    private String name;
    private List<String> totalPriceTypeList= Lists.newArrayList();

    public List<String> getTotalPriceTypeList() {
        return totalPriceTypeList;
    }

    public void setTotalPriceTypeList(List<String> totalPriceTypeList) {
        this.totalPriceTypeList = totalPriceTypeList;
    }

    public String getTotalPriceType() {
        return totalPriceType;
    }

    public void setTotalPriceType(String totalPriceType) {
        this.totalPriceType = totalPriceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
