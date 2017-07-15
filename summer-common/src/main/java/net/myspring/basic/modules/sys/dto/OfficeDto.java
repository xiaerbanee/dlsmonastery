package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class OfficeDto extends DataDto{

    private String name;
    private String type;
    private String parentId;
    private String parentIds;
    private boolean locked;
    private boolean enabled;
    private String areaId;
    private String officeRuleId;
    @CacheInput(inputKey = "officeRules",inputInstance = "officeRuleId",outputInstance = "name")
    private String officeRuleName;

    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;
    private String jointType;
    private String jointLevel;
    private BigDecimal point;
    private BigDecimal taskPoint;
    private String sort;
    private boolean allDataScope;
    private List<String> businessIdList=Lists.newArrayList();
    private List<String> leaderIdList= Lists.newArrayList();

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public boolean getAllDataScope() {
        return allDataScope;
    }

    public void setAllDataScope(boolean allDataScope) {
        this.allDataScope = allDataScope;
    }

    public List<String> getBusinessIdList() {
        return businessIdList;
    }

    public void setBusinessIdList(List<String> businessIdList) {
        this.businessIdList = businessIdList;
    }

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<String> getLeaderIdList() {
        return leaderIdList;
    }

    public void setLeaderIdList(List<String> leaderIdList) {
        this.leaderIdList = leaderIdList;
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

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
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
