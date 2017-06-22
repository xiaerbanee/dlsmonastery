package net.myspring.cloud.modules.sys.domain;


import net.myspring.cloud.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name="sys_gl_voucher_entry")
public class VoucherEntry extends IdEntity<VoucherEntry> {
    //摘要
    private String FExplanation;
    //科目编码
    private String FAccountId;
    //借方金额
    private BigDecimal FDebit;
    //贷方金额
    private BigDecimal FCredit;
    //凭证
    private String glVoucherId;

    public String getFExplanation() {
        return FExplanation;
    }

    public void setFExplanation(String FExplanation) {
        this.FExplanation = FExplanation;
    }

    public String getFAccountId() {
        return FAccountId;
    }

    public void setFAccountId(String FAccountId) {
        this.FAccountId = FAccountId;
    }

    public BigDecimal getFDebit() {
        return FDebit;
    }

    public void setFDebit(BigDecimal FDebit) {
        this.FDebit = FDebit;
    }

    public BigDecimal getFCredit() {
        return FCredit;
    }

    public void setFCredit(BigDecimal FCredit) {
        this.FCredit = FCredit;
    }

    public String getGlVoucherId() {
        return glVoucherId;
    }

    public void setGlVoucherId(String glVoucherId) {
        this.glVoucherId = glVoucherId;
    }
}
