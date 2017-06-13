package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;

import java.util.List;

public class ProductMonthPriceForm extends BaseForm<ProductMonthPrice> {

    private String month;
    private List<ProductMonthPriceDetailForm> ProductMonthPriceDetailList;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<ProductMonthPriceDetailForm> getProductMonthPriceDetailList() {
        return ProductMonthPriceDetailList;
    }

    public void setProductMonthPriceDetailList(List<ProductMonthPriceDetailForm> productMonthPriceDetailList) {
        ProductMonthPriceDetailList = productMonthPriceDetailList;
    }
}
