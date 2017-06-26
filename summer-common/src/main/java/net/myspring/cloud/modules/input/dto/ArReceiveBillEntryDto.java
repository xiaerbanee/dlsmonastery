package net.myspring.cloud.modules.input.dto;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/6/24.
 */
public class ArReceiveBillEntryDto {
    //应收金额
    private BigDecimal amount;
    //我方银行账号
    private String bankAcntNumber;
    //备注
    private String comment;
    //结算方式
    private String settleTypeNumber;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankAcntNumber() {
        return bankAcntNumber;
    }

    public void setBankAcntNumber(String bankAcntNumber) {
        this.bankAcntNumber = bankAcntNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSettleTypeNumber() {
        return settleTypeNumber;
    }

    public void setSettleTypeNumber(String settleTypeNumber) {
        this.settleTypeNumber = settleTypeNumber;
    }
}
