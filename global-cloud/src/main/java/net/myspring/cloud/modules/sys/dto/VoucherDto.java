package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.common.dto.DataDto;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.sys.domain.Voucher;

import java.time.LocalDate;
import java.util.List;

/**
 * 记录凭证审核状态
 * Created by lihx on 2017/4/5.
 */
public class VoucherDto extends DataDto<Voucher> {
    //业务日期
    private LocalDate FDate;
    private String createdName;
    private String companyId;
    private String status;
    //同步到金蝶后返回值
    private String outCode;
    private String kingdeeBookId;

    private Boolean deletable = false;
    private Boolean editable = false;

    private List<BdAccount> bdAccountList;
    private List<BdFlexItemGroup> bdFlexItemGroupList;
    private List<BdFlexItemProperty> bdFlexItemPropertyList;

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

    public LocalDate getFDate() {
        return FDate;
    }

    public void setFDate(LocalDate FDate) {
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

    public List<BdAccount> getBdAccountList() {
        return bdAccountList;
    }

    public void setBdAccountList(List<BdAccount> bdAccountList) {
        this.bdAccountList = bdAccountList;
    }

    public List<BdFlexItemGroup> getBdFlexItemGroupList() {
        return bdFlexItemGroupList;
    }

    public void setBdFlexItemGroupList(List<BdFlexItemGroup> bdFlexItemGroupList) {
        this.bdFlexItemGroupList = bdFlexItemGroupList;
    }

    public List<BdFlexItemProperty> getBdFlexItemPropertyList() {
        return bdFlexItemPropertyList;
    }

    public void setBdFlexItemPropertyList(List<BdFlexItemProperty> bdFlexItemPropertyList) {
        this.bdFlexItemPropertyList = bdFlexItemPropertyList;
    }
}
