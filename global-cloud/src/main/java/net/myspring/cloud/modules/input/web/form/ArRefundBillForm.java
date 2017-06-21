package net.myspring.cloud.modules.input.web.form;

import net.myspring.cloud.modules.input.dto.ApPayBillDto;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * 收款退款单
 * Created by lihx on 2017/6/20.
 */
public class ArRefundBillForm extends BaseForm<ApPayBillDto>{
    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
