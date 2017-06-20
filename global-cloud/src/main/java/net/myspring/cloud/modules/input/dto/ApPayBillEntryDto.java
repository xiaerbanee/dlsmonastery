package net.myspring.cloud.modules.input.dto;

/**
 * Created by lihx on 2017/6/20.
 */
public class ApPayBillEntryDto {
    //科目编码
    private String accountNumber;
    //银行账户编码
    private String bankAcntNumber;
    // 结算方式
    private String settleTypeNumber;
    // 摘要
    private String comment;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAcntNumber() {
        return bankAcntNumber;
    }

    public void setBankAcntNumber(String bankAcntNumber) {
        this.bankAcntNumber = bankAcntNumber;
    }

    public String getSettleTypeNumber() {
        return settleTypeNumber;
    }

    public void setSettleTypeNumber(String settleTypeNumber) {
        this.settleTypeNumber = settleTypeNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
