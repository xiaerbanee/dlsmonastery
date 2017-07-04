package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by lihx on 2017/4/18.
 */
public class DemoPhoneTypeQuery extends BaseQuery {
    private String name;
    private String productTypeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}
