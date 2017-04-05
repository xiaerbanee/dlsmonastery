package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.IdEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="api_gl_voucher_entry")
public class GlVoucherEntry extends IdEntity<GlVoucherEntry> {
    //摘要
    private String fexplanation;
    //科目编码
    private String faccountid;
    //借方金额
    private BigDecimal fdebit;
    //贷方金额
    private BigDecimal fcredit;
    //凭证
    private GlVoucher glVoucher;
    private String glVoucherId;
    private List<GlVoucherEntryFlow> glVoucherEntryFlowList = Lists.newArrayList();
    private List<String> glVoucherEntryFlowIdList = Lists.newArrayList();

    public String getFexplanation() {
        return fexplanation;
    }

    public void setFexplanation(String fexplanation) {
        this.fexplanation = fexplanation;
    }

    public String getFaccountid() {
        return faccountid;
    }

    public void setFaccountid(String faccountid) {
        this.faccountid = faccountid;
    }

    public BigDecimal getFdebit() {
        return fdebit;
    }

    public void setFdebit(BigDecimal fdebit) {
        this.fdebit = fdebit;
    }

    public BigDecimal getFcredit() {
        return fcredit;
    }

    public void setFcredit(BigDecimal fcredit) {
        this.fcredit = fcredit;
    }

    public GlVoucher getGlVoucher() {
        return glVoucher;
    }

    public void setGlVoucher(GlVoucher glVoucher) {
        this.glVoucher = glVoucher;
    }

    public String getGlVoucherId() {
        return glVoucherId;
    }

    public void setGlVoucherId(String glVoucherId) {
        this.glVoucherId = glVoucherId;
    }

    public List<GlVoucherEntryFlow> getGlVoucherEntryFlowList() {
        return glVoucherEntryFlowList;
    }

    public void setGlVoucherEntryFlowList(List<GlVoucherEntryFlow> glVoucherEntryFlowList) {
        this.glVoucherEntryFlowList = glVoucherEntryFlowList;
    }

    public List<String> getGlVoucherEntryFlowIdList() {
        return glVoucherEntryFlowIdList;
    }

    public void setGlVoucherEntryFlowIdList(List<String> glVoucherEntryFlowIdList) {
        this.glVoucherEntryFlowIdList = glVoucherEntryFlowIdList;
    }
}
