package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String companyId;
    private String outId;

    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "positions",inputInstance = "positionId",outputInstance = "name")
    private String positionName;
    @CacheInput(inputKey = "accounts",inputInstance = "leaderId",outputInstance = "loginName")
    private String leaderName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "entryDate")
    private LocalDate entryDate;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "leaveDate")
    private LocalDate leaveDate;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "regularDate")
    private LocalDate regularDate;
    @CacheInput(inputKey = "companys",inputInstance = "companyId",outputInstance = "name")
    private String companyName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "status")
    private String employeeStatus;

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
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

}
