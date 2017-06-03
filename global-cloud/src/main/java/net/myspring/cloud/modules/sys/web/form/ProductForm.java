package net.myspring.cloud.modules.sys.web.form;

import java.util.List;

/**
 * Created by lihx on 2017/6/2.
 */
public class ProductForm {
    private List<String> productNameList;
    private String json;

    public List<String> getProductNameList() {
        return productNameList;
    }

    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
