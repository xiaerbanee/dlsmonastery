package net.myspring.cloud.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.Voucher;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
public class VoucherDto extends DataDto<Voucher> {
    private String FDate;
    private String createdName;
    private String companyId;
    private String status;
    private String outCode;
    private String kingdeeBookId;
    //明细
    private List<VoucherEntryDto> voucherEntryDtoList = Lists.newArrayList();
    private List<String> voucherEntryIdList = Lists.newArrayList();

    private Boolean deletable = false;
    private Boolean editable = false;
    private List<String> actionList;

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

    public List<VoucherEntryDto> getVoucherEntryDtoList() {
        return voucherEntryDtoList;
    }

    public void setVoucherEntryDtoList(List<VoucherEntryDto> voucherEntryDtoList) {
        this.voucherEntryDtoList = voucherEntryDtoList;
    }

    public List<String> getVoucherEntryIdList() {
        return voucherEntryIdList;
    }

    public void setVoucherEntryIdList(List<String> voucherEntryIdList) {
        this.voucherEntryIdList = voucherEntryIdList;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }
}
