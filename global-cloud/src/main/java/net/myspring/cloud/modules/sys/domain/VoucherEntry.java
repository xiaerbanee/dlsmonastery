package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.cloud.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="sys_gl_voucher_entry")
public class VoucherEntry extends IdEntity<VoucherEntry> {
    //摘要
    private String fExplanation;
    //科目编码
    private String fAccountId;
    //借方金额
    private BigDecimal fDebit;
    //贷方金额
    private BigDecimal fCredit;
    //凭证
    private String glVoucherId;


    public String getfExplanation() {
        return fExplanation;
    }

    public void setfExplanation(String fExplanation) {
        this.fExplanation = fExplanation;
    }

    public String getfAccountId() {
        return fAccountId;
    }

    public void setfAccountId(String fAccountId) {
        this.fAccountId = fAccountId;
    }

    public BigDecimal getfDebit() {
        return fDebit;
    }

    public void setfDebit(BigDecimal fDebit) {
        this.fDebit = fDebit;
    }

    public BigDecimal getfCredit() {
        return fCredit;
    }

    public void setfCredit(BigDecimal fCredit) {
        this.fCredit = fCredit;
    }

    public String getGlVoucherId() {
        return glVoucherId;
    }

    public void setGlVoucherId(String glVoucherId) {
        this.glVoucherId = glVoucherId;
    }
}
