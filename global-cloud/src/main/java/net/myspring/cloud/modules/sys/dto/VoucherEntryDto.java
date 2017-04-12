package net.myspring.cloud.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.VoucherEntry;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
public class VoucherEntryDto extends DataDto<VoucherEntry> {
    //摘要
    private String fExplanation;
    //科目编码
    private String fAccountId;
    //借方金额
    private BigDecimal fDebit;
    //贷方金额
    private BigDecimal fCredit;
    private VoucherDto voucherDto;
    private List<VoucherEntryFlowDto> voucherEntryFlowDtoList = Lists.newArrayList();
    private List<String> voucherEntryFlowIdList = Lists.newArrayList();

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

    public VoucherDto getVoucherDto() {
        return voucherDto;
    }

    public void setVoucherDto(VoucherDto voucherDto) {
        this.voucherDto = voucherDto;
    }

    public List<VoucherEntryFlowDto> getVoucherEntryFlowDtoList() {
        return voucherEntryFlowDtoList;
    }

    public void setVoucherEntryFlowDtoList(List<VoucherEntryFlowDto> voucherEntryFlowDtoList) {
        this.voucherEntryFlowDtoList = voucherEntryFlowDtoList;
    }

    public List<String> getVoucherEntryFlowIdList() {
        return voucherEntryFlowIdList;
    }

    public void setVoucherEntryFlowIdList(List<String> voucherEntryFlowIdList) {
        this.voucherEntryFlowIdList = voucherEntryFlowIdList;
    }
}
