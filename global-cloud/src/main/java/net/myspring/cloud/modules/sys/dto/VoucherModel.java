package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;

import java.util.List;
import java.util.Map;

/**
 * 核算维度
 * Created by lihx on 2017/6/22.
 */
public class VoucherModel {
    private List<BdAccount> bdAccountList;
    private List<BdFlexItemGroup> bdFlexItemGroupList;
    private List<BdFlexItemProperty> bdFlexItemPropertyList;

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
