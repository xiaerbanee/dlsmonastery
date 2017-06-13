package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.Product;

import java.util.List;

/**
 * Created by zhangyf on 2017/6/8.
 */
public class ProductBatchForm extends BaseForm<Product>{

    private String productList;

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }
}
