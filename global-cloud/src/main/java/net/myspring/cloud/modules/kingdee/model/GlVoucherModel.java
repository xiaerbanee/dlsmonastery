package net.myspring.cloud.modules.kingdee.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;
import net.myspring.cloud.modules.kingdee.dto.BdFlexItemGroupDto;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/10.
 */
public class GlVoucherModel {
    List<BdAccount> bdAccountList = Lists.newArrayList();
    List<BdFlexItemGroupDto> bdFlexItemGroupDtoList = Lists.newArrayList();
    List<BdFlexItemProperty> bdFlexItemPropertyList = Lists.newArrayList();
    Map<String, Map<String, String>> result = Maps.newHashMap();

    //核算维度
    public enum SubjectGroup {
        供应商,
        部门,
        客户,
        其他类,
        费用类,
        员工,
        银行账号
    };

    public List<BdAccount> getBdAccountList() {
        return bdAccountList;
    }

    public void setBdAccountList(List<BdAccount> bdAccountList) {
        this.bdAccountList = bdAccountList;
    }

    public List<BdFlexItemGroupDto> getBdFlexItemGroupDtoList() {
        return bdFlexItemGroupDtoList;
    }

    public void setBdFlexItemGroupDtoList(List<BdFlexItemGroupDto> bdFlexItemGroupDtoList) {
        this.bdFlexItemGroupDtoList = bdFlexItemGroupDtoList;
    }

    public List<BdFlexItemProperty> getBdFlexItemPropertyList() {
        return bdFlexItemPropertyList;
    }

    public void setBdFlexItemPropertyList(List<BdFlexItemProperty> bdFlexItemPropertyList) {
        this.bdFlexItemPropertyList = bdFlexItemPropertyList;
    }

    public Map<String, Map<String, String>> getResult() {
        return result;
    }

    public void setResult(Map<String, Map<String, String>> result) {
        this.result = result;
    }
}
