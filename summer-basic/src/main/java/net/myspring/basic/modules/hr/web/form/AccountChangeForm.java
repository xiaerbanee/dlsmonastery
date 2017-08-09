package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */
public class AccountChangeForm extends BaseForm<AccountChange> {

    private String type;
    private String accountId;
    private String officeId;
    private String positionId;
    private String leaderId;
    private String mobilePhone;
    private String idcard;
    private LocalDate entryDate;
    private BigDecimal salary;
    private LocalDate leaveDate;
    private LocalDate regularDate;
    private String bankNumber;
    private String remarks;
    private String oldValue;
    private String newValue;
    @CacheInput(inputKey = "accounts",inputInstance = "accountId",outputInstance = "loginName")
    private String accountName;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "positions",inputInstance = "positionId",outputInstance = "name")
    private String positionName;
    @CacheInput(inputKey = "accounts",inputInstance = "leaderId",outputInstance = "loginName")
    private String leaderName;
    private String positionIds;
    private List<String> positionIdList=Lists.newArrayList();
    @CacheInput(inputKey = "positions",inputInstance = "positionIdList",outputInstance = "name")
    private List<String> positionNameList=Lists.newArrayList();

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

    public List<String> getPositionNameList() {
        return positionNameList;
    }

    public void setPositionNameList(List<String> positionNameList) {
        this.positionNameList = positionNameList;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public LocalDate getRegularDate() {
        return regularDate;
    }

    public void setRegularDate(LocalDate regularDate) {
        this.regularDate = regularDate;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
