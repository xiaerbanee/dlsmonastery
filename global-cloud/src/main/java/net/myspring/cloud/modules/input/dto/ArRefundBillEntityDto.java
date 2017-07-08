package net.myspring.cloud.modules.input.dto;

/**
 * Created by lihx on 2017/6/20.
 */
public class ArRefundBillEntityDto {
    private String BankAcntNumber;
    private String FSettleTypeIdNumber;
    private String accountNumber;
    private String note;

    public String getBankAcntNumber() {
        return BankAcntNumber;
    }

    public void setBankAcntNumber(String bankAcntNumber) {
        BankAcntNumber = bankAcntNumber;
    }

    public String getFSettleTypeIdNumber() {
        return FSettleTypeIdNumber;
    }

    public void setFSettleTypeIdNumber(String FSettleTypeIdNumber) {
        this.FSettleTypeIdNumber = FSettleTypeIdNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
