package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.IdEntity;
import net.myspring.util.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="api_k3cloud_gl_voucher_entry")
public class K3cloudGlVoucherEntry extends IdEntity<K3cloudGlVoucherEntry> {
    //摘要
    private String fexplanation;
    //科目编码
    private String faccountid;
    //借方金额
    private BigDecimal fdebit;
    //贷方金额
    private BigDecimal fcredit;
    //凭证
    private K3cloudGlVoucher k3cloudGlVoucher;
    private String k3cloudGlVoucherId;
    private List<K3cloudGlVoucherEntryFlow> k3cloudGlVoucherEntryFlowList = Lists.newArrayList();
    private List<String> k3cloudGlVoucherEntryFlowIdList = Lists.newArrayList();

    //#科目全名
    @Transient
    private String FAcctFullName;

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

    public K3cloudGlVoucher getK3cloudGlVoucher() {
        return k3cloudGlVoucher;
    }

    public void setK3cloudGlVoucher(K3cloudGlVoucher k3cloudGlVoucher) {
        this.k3cloudGlVoucher = k3cloudGlVoucher;
    }

    public String getK3cloudGlVoucherId() {
        if(StringUtils.isBlank(k3cloudGlVoucherId) && k3cloudGlVoucher!=null) {
            k3cloudGlVoucherId = k3cloudGlVoucher.getId();
        }
        return k3cloudGlVoucherId;
    }

    public void setK3cloudGlVoucherId(String k3cloudGlVoucherId) {
        this.k3cloudGlVoucherId = k3cloudGlVoucherId;
    }

    public List<K3cloudGlVoucherEntryFlow> getK3cloudGlVoucherEntryFlowList() {
        return k3cloudGlVoucherEntryFlowList;
    }

    public void setK3cloudGlVoucherEntryFlowList(List<K3cloudGlVoucherEntryFlow> k3cloudGlVoucherEntryFlowList) {
        this.k3cloudGlVoucherEntryFlowList = k3cloudGlVoucherEntryFlowList;
    }

    public List<String> getK3cloudGlVoucherEntryFlowIdList() {
        if(CollectionUtil.isEmpty(k3cloudGlVoucherEntryFlowIdList) && CollectionUtil.isNotEmpty(k3cloudGlVoucherEntryFlowList)) {
            k3cloudGlVoucherEntryFlowIdList = CollectionUtil.extractToList(k3cloudGlVoucherEntryFlowList,"id");
        }
        return k3cloudGlVoucherEntryFlowIdList;
    }

    public void setK3cloudGlVoucherEntryFlowIdList(List<String> k3cloudGlVoucherEntryFlowIdList) {
        this.k3cloudGlVoucherEntryFlowIdList = k3cloudGlVoucherEntryFlowIdList;
    }

    public String getFAcctFullName() {
        return FAcctFullName;
    }

    public void setFAcctFullName(String FAcctFullName) {
        this.FAcctFullName = FAcctFullName;
    }
}
