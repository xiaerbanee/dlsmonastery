package net.myspring.cloud.modules.input.web.form;

import net.myspring.cloud.modules.input.dto.ApPayBillDto;
import net.myspring.cloud.modules.input.dto.ArOtherRecAbleDto;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * 付款单
 * Created by lihx on 2017/6/20.
 */
public class ApPayBillForm extends BaseForm<ApPayBillDto>{
    private LocalDate billDate;
    private String json;

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
