package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by sungm on 2017/6/1.
 */
public class ProductMonthPriceQuery extends BaseQuery {
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
