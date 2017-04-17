package net.myspring.basic.modules.hr.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DataScopeEnum;
import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.JobDto;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */

public class PositionForm extends DataForm<Position> {

    private List<String> permissionIdList;
    private String jobId;
    private String name;
    private String reportName;
    private String dataScope;
    private String permission;
    private String sort;
    private String remarks;
    private String permissionIdStr;
    private TreeNode permissionTree;
    private List<JobDto> jobList= Lists.newArrayList();
    private Map<Integer,String> dataScopeMap= Maps.newHashMap();

    public TreeNode getPermissionTree() {
        return permissionTree;
    }

    public void setPermissionTree(TreeNode permissionTree) {
        this.permissionTree = permissionTree;
    }

    public List<JobDto> getJobList() {
        return jobList;
    }

    public void setJobList(List<JobDto> jobList) {
        this.jobList = jobList;
    }

    public Map<Integer,String> getDataScopeMap() {
        return dataScopeMap;
    }

    public void setDataScopeMap(Map<Integer,String> dataScopeMap) {
        this.dataScopeMap = dataScopeMap;
    }

    public List<String> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<String> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPermissionIdStr() {
        return permissionIdStr;
    }

    public void setPermissionIdStr(String permissionIdStr) {
        this.permissionIdStr = permissionIdStr;
    }
}
