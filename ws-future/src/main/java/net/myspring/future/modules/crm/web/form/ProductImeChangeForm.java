package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductIme;

import java.util.List;


public class ProductImeChangeForm extends BaseForm<ProductIme> {

    private String ime;
    private String productName;

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
