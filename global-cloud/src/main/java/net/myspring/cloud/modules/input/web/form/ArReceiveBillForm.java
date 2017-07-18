package net.myspring.cloud.modules.input.web.form;

import net.myspring.cloud.modules.input.dto.ArReceiveBillDto;
import net.myspring.common.form.BaseForm;

/**
 * 收款单
 * Created by lihx on 2017/7/20.
 */
public class ArReceiveBillForm extends BaseForm<ArReceiveBillDto>{
    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
