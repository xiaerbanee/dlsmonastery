package net.myspring.cloud.modules.input.web.form;

import net.myspring.cloud.modules.input.dto.CnJournalForBankDto;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;
import java.util.List;

/**
 * 手工日记账之銀行存款日记账
 * Created by lihx on 2017/6/9.
 */
public class CnJournalForBankForm extends BaseForm<CnJournalForBankDto>{
    private LocalDate billDate;
    private String accountNumber;
    private String json;

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
