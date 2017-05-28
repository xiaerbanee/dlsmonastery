package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.PricesystemChange;

/**
 * Created by haos on 2017/5/17.
 */
public class PricesystemChangeForm extends BaseForm<PricesystemChange> {
    private String productId;
    private String remarks;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
