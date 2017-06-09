package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductIme;

import java.util.List;


public class ProductImeBatchChangeForm extends BaseForm<ProductIme> {

    private List<ProductImeChangeForm> productImeChangeFormList;

    public List<ProductImeChangeForm> getProductImeChangeFormList() {
        return productImeChangeFormList;
    }

    public void setProductImeChangeFormList(List<ProductImeChangeForm> productImeChangeFormList) {
        this.productImeChangeFormList = productImeChangeFormList;
    }
}
