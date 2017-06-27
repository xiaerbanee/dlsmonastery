package net.myspring.cloud.modules.input.dto;

import net.myspring.cloud.modules.sys.dto.VoucherEntryDto;
import net.myspring.cloud.modules.sys.dto.VoucherEntryFlowDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lihx on 2017/6/27.
 */
public class GlVoucherFEntityDto {
    //摘要
    private String explanation;
    //科目
    private String accountNumber;
    //核算维度
    private List<VoucherEntryFlowDto> voucherEntryFlowDtoList;
    //贷方总金额
    private BigDecimal credit;
    //借方总金额
    private BigDecimal debit;

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public List<VoucherEntryFlowDto> getVoucherEntryFlowDtoList() {
        return voucherEntryFlowDtoList;
    }

    public void setVoucherEntryFlowDtoList(List<VoucherEntryFlowDto> voucherEntryFlowDtoList) {
        this.voucherEntryFlowDtoList = voucherEntryFlowDtoList;
    }
}
