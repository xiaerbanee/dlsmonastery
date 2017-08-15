package net.myspring.cloud.modules.input.web.form;

import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * 入库成本调整单
 */
public class HsAdjustmentBillForm extends BaseForm<HsAdjustmentBillForm>{
    private LocalDate billDate;
    private String supplierNumber = "B001";
    private String json;

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
