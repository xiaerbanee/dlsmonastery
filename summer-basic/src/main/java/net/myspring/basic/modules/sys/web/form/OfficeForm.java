package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.constant.TreeConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.cahe.annotation.CacheInput;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */

public class OfficeForm extends BaseForm<Office> {

    private String parentId;
    private String name;
    private String jointType;
    private BigDecimal point;
    private BigDecimal taskPoint;
    private String sort;
    private TreeNode officeTree;
    private String type;
    private List<String> officeIdList=Lists.newArrayList();
    private String officeIdStr;
    private List<OfficeRuleDto>  officeRuleList= Lists.newArrayList();
    private List<String> jointTypeList= Lists.newArrayList();
    private String officeRuleId;
    private List<String> leaderIdList=Lists.newArrayList();
    private List<String> officeTypeList=Lists.newArrayList();
    private List<String> joinLevelList=Lists.newArrayList();
    private Integer level;
    private String parentIds;
    private Office parent;
    private String areaId;
    private String jointLevel;

    public List<String> getJoinLevelList() {
        return joinLevelList;
    }

    public void setJoinLevelList(List<String> joinLevelList) {
        this.joinLevelList = joinLevelList;
    }

    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;
    @CacheInput(inputKey = "accounts",inputInstance = "leaderIdList",outputInstance = "loginName")
    private List<String> leaderNameList=Lists.newArrayList();

    public Integer getLevel() {
        return (getParentIds().split(CharConstant.COMMA)).length+1;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Office getParent() {
        return parent;
    }

    public void setParent(Office parent) {
        this.parent = parent;
    }

    public String getParentIds() {
        if(StringUtils.isBlank(parentIds)){
            if(parent!=null){
                this.parentIds=parent.getParentIds()+parent.getId()+CharConstant.COMMA;
            }else {
                this.parentIds= TreeConstant.ROOT_PARENT_IDS;
            }
        }
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOfficeTypeList() {
        return officeTypeList;
    }

    public void setOfficeTypeList(List<String> officeTypeList) {
        this.officeTypeList = officeTypeList;
    }

    public List<String> getLeaderNameList() {
        return leaderNameList;
    }

    public void setLeaderNameList(List<String> leaderNameList) {
        this.leaderNameList = leaderNameList;
    }

    public List<String> getLeaderIdList() {
        return leaderIdList;
    }

    public void setLeaderIdList(List<String> leaderIdList) {
        this.leaderIdList = leaderIdList;
    }

    public TreeNode getOfficeTree() {
        return officeTree;
    }

    public void setOfficeTree(TreeNode officeTree) {
        this.officeTree = officeTree;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public String getOfficeIdStr() {
        return officeIdStr;
    }

    public void setOfficeIdStr(String officeIdStr) {
        this.officeIdStr = officeIdStr;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<OfficeRuleDto> getOfficeRuleList() {
        return officeRuleList;
    }

    public void setOfficeRuleList(List<OfficeRuleDto> officeRuleList) {
        this.officeRuleList = officeRuleList;
    }

    public List<String> getJointTypeList() {
        return jointTypeList;
    }

    public void setJointTypeList(List<String> jointTypeList) {
        this.jointTypeList = jointTypeList;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeRuleId() {
        if(OfficeTypeEnum.SUPPORT.name().equals(type)){
            this.officeRuleId=null;
        }
        return officeRuleId;
    }

    public void setOfficeRuleId(String officeRuleId) {
        this.officeRuleId = officeRuleId;
    }

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
