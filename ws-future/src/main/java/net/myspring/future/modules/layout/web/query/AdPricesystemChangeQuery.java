package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdPricesystemChangeQuery extends BaseQuery{
    private String productName;
    private String productCode;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
