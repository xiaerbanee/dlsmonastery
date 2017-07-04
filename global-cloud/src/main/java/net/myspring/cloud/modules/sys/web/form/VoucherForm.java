package net.myspring.cloud.modules.sys.web.form;

import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/12.
 */
public class VoucherForm extends BaseForm<Voucher> {
    private LocalDate FDate;
    private String json;

    //附加
    private List<BdAccount> bdAccountList;
    private List<BdFlexItemGroup> bdFlexItemGroupList;
    private List<BdFlexItemProperty> bdFlexItemPropertyList;


    public LocalDate getFDate() {
        return FDate;
    }

    public void setFDate(LocalDate FDate) {
        this.FDate = FDate;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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
