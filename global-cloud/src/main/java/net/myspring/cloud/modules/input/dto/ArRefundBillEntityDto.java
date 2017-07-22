package net.myspring.cloud.modules.input.dto;

/**
 * Created by lihx on 2017/6/20.
 */
public class ArRefundBillEntityDto {
    //银行账户编码
    private String BankAcntNumber;
    //结算方式编码
    private String FSettleTypeIdNumber;
    //备注
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
