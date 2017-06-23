package net.myspring.cloud.modules.input.web.form;

import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * Created by liuj on 2017/5/11.
 */
public class SalStockForm extends BaseForm<SalStockForm>{
    private String stockNumber;
    private LocalDate billDate;
    private String json;

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
