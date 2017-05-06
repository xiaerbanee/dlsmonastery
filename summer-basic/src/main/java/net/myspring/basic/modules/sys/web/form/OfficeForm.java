package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */

public class OfficeForm extends DataForm<Office> {

    private String parentId;
    private String name;
    private String jointType;
    private String point;
    private String taskPoint;
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

    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;
    @CacheInput(inputKey = "accounts",inputInstance = "leaderIdList",outputInstance = "loginName")
    private List<String> leaderNameList=Lists.newArrayList();


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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(String taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
