package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.TreeDto;
import net.myspring.basic.common.enums.OfficeRuleEnum;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.util.Map;

/**
 * Created by wangzm on 2017/4/22.
 */
public class OfficeRuleDto extends TreeDto<OfficeRule> {
    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;
    private String name;
    private String code;
    private Integer type;
    private Integer value;

    public String getTypeLabel() {
        if(type!=null){
            Map<Integer, String> map = OfficeRuleEnum.getMap();
            return map.get(type);
        }
        return "";
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
