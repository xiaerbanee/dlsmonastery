package net.myspring.basic.modules.sys.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/4/5.
 */
public class OfficeDto{

    private String id;
    private String name;
    private String type;
    private BigDecimal point;
    private String parentId;
    private boolean locked;
    private boolean enabled;
    private BigDecimal taskPoint;
    private String officeRuleId;
    @CacheInput(inputKey = "officeRules",inputInstance = "officeRuleId",outputInstance = "name")
    private String officeRuleName;

    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getOfficeRuleId() {
        return officeRuleId;
    }

    public void setOfficeRuleId(String officeRuleId) {
        this.officeRuleId = officeRuleId;
    }

    public String getOfficeRuleName() {
        return officeRuleName;
    }

    public void setOfficeRuleName(String officeRuleName) {
        this.officeRuleName = officeRuleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getFullName(){
        return (StringUtils.isBlank(officeRuleName)?"顶级业务部门":officeRuleName)+ CharConstant.DATE_RANGE_SPLITTER+name;
    }

}
