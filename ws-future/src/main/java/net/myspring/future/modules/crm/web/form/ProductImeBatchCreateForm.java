package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductIme;

import java.util.List;


public class ProductImeBatchCreateForm extends BaseForm<ProductIme> {



    private List<ProductImeCreateForm> productImeCreateFormList;

    public List<ProductImeCreateForm> getProductImeCreateFormList() {
        return productImeCreateFormList;
    }

    public void setProductImeCreateFormList(List<ProductImeCreateForm> productImeCreateFormList) {
        this.productImeCreateFormList = productImeCreateFormList;
    }
}
