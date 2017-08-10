package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * Created by liuj on 2017/3/19.
 */

public class AccountForm extends BaseForm<Account> {
    private String password;
    private String type;
    private String employeeId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String loginName;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String leaderId;
    @CacheInput(inputKey = "accounts",inputInstance = "leaderId",outputInstance = "loginName")
    private String leaderName;
    private Boolean viewReport;
    private String outId;
    private String outPassword;
    private String positionId;
    private List<String> permissionIdList=Lists.newArrayList();
    private String positionIds;
    private List<String> positionIdList=Lists.newArrayList();
    private String officeIds;
    private List<String> officeIdList=Lists.newArrayList();
    @CacheInput(inputKey = "offices",inputInstance = "officeIdList",outputInstance = "name")
    private List<String> officeListName=Lists.newArrayList();

    public String getPositionIds() {
        return positionIds;
    }

    public void setPositionIds(String positionIds) {
        this.positionIds = positionIds;
    }

    public List<String> getPositionIdList() {
        if(CollectionUtil.isEmpty(positionIdList)&& StringUtils.isNotBlank(positionIds)){
            this.positionIdList=StringUtils.getSplitList(positionIds, CharConstant.COMMA);
        }
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
    }

    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }

    public List<String> getOfficeIdList() {
        if(CollectionUtil.isEmpty(officeIdList)&&StringUtils.isNotBlank(officeIds)){
            this.officeIdList=StringUtils.getSplitList(officeIds, CharConstant.COMMA);
        }
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<String> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<String> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getOfficeListName() {
        return officeListName;
    }

    public void setOfficeListName(List<String> officeListName) {
        this.officeListName = officeListName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Boolean getViewReport() {
        return viewReport;
    }

    public void setViewReport(Boolean viewReport) {
        this.viewReport = viewReport;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOutPassword() {
        return outPassword;
    }

    public void setOutPassword(String outPassword) {
        this.outPassword = outPassword;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

}
