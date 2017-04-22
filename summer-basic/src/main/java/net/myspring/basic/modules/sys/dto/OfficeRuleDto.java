package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.TreeDto;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/4/22.
 */
public class OfficeRuleDto extends TreeDto<OfficeRule> {
    private String parentId;
    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;
    private String name;
    private String code;

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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
}
