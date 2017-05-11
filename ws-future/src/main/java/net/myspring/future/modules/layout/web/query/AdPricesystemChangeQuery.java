package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdPricesystemChangeQuery extends BaseQuery{
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
