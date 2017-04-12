package net.myspring.cloud.modules.kingdee.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/10.
 */
public class GlVoucherDto {
    List<BdAccount> bdAccountList = Lists.newArrayList();
    List<BdFlexItemGroupDto> bdFlexItemGroupDtoList = Lists.newArrayList();
    List<BdFlexItemProperty> bdFlexItemPropertyList = Lists.newArrayList();
    Map<String, Map<String, String>> result = Maps.newHashMap();

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
