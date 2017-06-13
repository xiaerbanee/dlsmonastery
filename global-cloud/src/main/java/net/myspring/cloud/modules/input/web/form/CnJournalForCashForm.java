package net.myspring.cloud.modules.input.web.form;

import net.myspring.cloud.modules.input.dto.CnJournalForCashDto;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/6/8.
 */
public class CnJournalForCashForm extends BaseForm<CnJournalForCashDto>{
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
