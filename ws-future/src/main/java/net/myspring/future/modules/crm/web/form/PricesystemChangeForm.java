package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.PricesystemChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haos on 2017/5/17.
 */
public class PricesystemChangeForm extends BaseForm<PricesystemChange> {
    private List<String> productIds = new ArrayList<>();
    private String remarks;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
