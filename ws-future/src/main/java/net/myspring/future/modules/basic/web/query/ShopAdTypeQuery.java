package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;

import java.util.List;

/**
 * Created by lihx on 2017/4/19.
 */
public class ShopAdTypeQuery extends BaseQuery {
    private String totalPriceType;
    private String name;

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
