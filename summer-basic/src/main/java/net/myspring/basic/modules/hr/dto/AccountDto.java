package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/3/19.
 */
public class AccountDto extends DataDto<Account> {
    private String loginName;
    private String type;
    private String positionId;
    private String officeId;
    private String leaderId;
    private String employeeId;
    private String outId;
    private String companyName;
    private String officeName;
    private String positionName;
    private String leaderName;
    private LocalDate entryDate;
    private LocalDate leaveDate;
    private LocalDate regularDate;
    private String employeeName;
    private String employeeStatus;
    private String positionIds;
    private List<String> positionIdList=Lists.newArrayList();
    private String officeIds;
    private List<String> officeIdList=Lists.newArrayList();
    @CacheInput(inputKey = "positions",inputInstance = "positionIdList",outputInstance = "name")
    private List<String> positionNameList=Lists.newArrayList();
    @CacheInput(inputKey = "offices",inputInstance = "officeIdList",outputInstance = "name")
    private List<String> officeNameList=Lists.newArrayList();

    private String dataScopeOfficeName;

    public String getDataScopeOfficeName() {
        if(StringUtils.isBlank(dataScopeOfficeName)&&CollectionUtil.isNotEmpty(officeNameList)){
            this.dataScopeOfficeName=StringUtils.join(officeNameList,CharConstant.COMMA);
        }
        return dataScopeOfficeName;
    }

    public void setDataScopeOfficeName(String dataScopeOfficeName) {
        this.dataScopeOfficeName = dataScopeOfficeName;
    }

    public String getPositionIds() {
        return positionIds;
    }

    public void setPositionIds(String positionIds) {
        this.positionIds = positionIds;
    }

    public List<String> getPositionIdList() {
        if(CollectionUtil.isEmpty(positionIdList)&&StringUtils.isNotBlank(positionIds)){
            this.positionIdList=StringUtils.getSplitList(positionIds, CharConstant.COMMA);
        }
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
    }

    public List<String> getPositionNameList() {
        return positionNameList;
    }

    public void setPositionNameList(List<String> positionNameList) {
        this.positionNameList = positionNameList;
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

    public List<String> getOfficeNameList() {
        return officeNameList;
    }

    public void setOfficeNameList(List<String> officeNameList) {
        this.officeNameList = officeNameList;
    }

    private boolean admin=RequestUtils.getAdmin();

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCompanyName() {
        if(StringUtils.isBlank(companyName)){
            this.companyName= RequestUtils.getCompanyName();
        }
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public LocalDate getRegularDate() {
        return regularDate;
    }

    public void setRegularDate(LocalDate regularDate) {
        this.regularDate = regularDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getPositionNames(){
        if(CollectionUtil.isNotEmpty(positionNameList)){
            return StringUtils.join(positionNameList,CharConstant.COMMA);
        }
        return "";
    }

}
