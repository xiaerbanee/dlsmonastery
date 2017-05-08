package net.myspring.cloud.modules.input.web.form;


/**
 * Created by lihx on 2017/4/25.
 */
public class BatchCashDepositJournalForm {
    private String billDate;
    private String data;

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
