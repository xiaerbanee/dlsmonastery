package net.myspring.cloud.modules.sys.web.form;

import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/12.
 */
public class VoucherForm extends BaseForm<Voucher> {
    private LocalDate fdate;
    private String json;

    public LocalDate getFdate() {
        return fdate;
    }

    public void setFdate(LocalDate fdate) {
        this.fdate = fdate;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
