package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductImeSale;

public class ProductImeSaleDetailForm extends BaseForm<ProductImeSale> {

    private String productImeId;
    private String saleShopId;

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getSaleShopId() {
        return saleShopId;
    }

    public void setSaleShopId(String saleShopId) {
        this.saleShopId = saleShopId;
    }
}
