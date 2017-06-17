package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.web.form.DepotAdApplyForm;
import net.myspring.future.modules.layout.domain.AdApply;

import java.util.List;

/**
 * Created by zhangyf on 2017/6/16.
 */
public class AdApplyGoodsForm extends BaseForm<AdApply>{
    private String productId;
    private List<DepotAdApplyForm> depotAdApplyForms;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<DepotAdApplyForm> getDepotAdApplyForms() {
        return depotAdApplyForms;
    }

    public void setDepotAdApplyForms(List<DepotAdApplyForm> depotAdApplyForms) {
        this.depotAdApplyForms = depotAdApplyForms;
    }
}
