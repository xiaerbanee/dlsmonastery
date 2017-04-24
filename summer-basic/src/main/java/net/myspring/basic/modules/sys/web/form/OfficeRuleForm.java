package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/22.
 */
public class OfficeRuleForm extends DataForm<OfficeRule> {
    private String parentIds;
    private String parentId;
    @CacheInput(inputKey = "officeRules",inputInstance = "parentId",outputInstance = "name")
    private String parentName;
    private String name;
    private String code;
    private Map<Boolean,String> boolMap= Maps.newHashMap();
    private boolean hasPoint;
    private List<OfficeRuleDto> officeRuleList= Lists.newArrayList();

    public List<OfficeRuleDto> getOfficeRuleList() {
        return officeRuleList;
    }

    public void setOfficeRuleList(List<OfficeRuleDto> officeRuleList) {
        this.officeRuleList = officeRuleList;
    }

    public boolean getHasPoint() {
        return hasPoint;
    }

    public void setHasPoint(boolean hasPoint) {
        this.hasPoint = hasPoint;
    }

    public Map<Boolean, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<Boolean, String> boolMap) {
        this.boolMap = boolMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}