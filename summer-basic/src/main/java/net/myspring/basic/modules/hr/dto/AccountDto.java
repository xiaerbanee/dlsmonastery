package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import io.reactivex.netty.channel.StringTransformer;
import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.common.enums.DataScopeEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private boolean viewReport;
    private List<String> officeIdList= Lists.newArrayList();

    @CacheInput(inputKey = "positions",inputInstance = "positionId",outputInstance = "dataScope")
    private Integer dataScope;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "positions",inputInstance = "positionId",outputInstance = "name")
    private String positionName;
    @CacheInput(inputKey = "accounts",inputInstance = "leaderId",outputInstance = "loginName")
    private String leaderName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "regularDate")
    private LocalDate regularDate;
    @CacheInput(inputKey = "companys",inputInstance = "companyId",outputInstance = "name")
    private String companyName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "status")
    private String employeeStatus;
    @CacheInput(inputKey = "positions",inputInstance = "positionId",outputInstance = "dataScope")
    private Integer positionDataScope;
    @CacheInput(inputKey = "offices",inputInstance = "officeIdList",outputInstance = "name")
    private List<String> officeListName=Lists.newArrayList();

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

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
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

    public String getDataScopeLabel(){
        Map<Integer, String> map = DataScopeEnum.getMap();
        if(CollectionUtil.isNotEmpty(map)){
            return map.get(positionDataScope);
        }
        return "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isViewReport() {
        return viewReport;
    }

    public void setViewReport(boolean viewReport) {
        this.viewReport = viewReport;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Integer getPositionDataScope() {
        return positionDataScope;
    }

    public void setPositionDataScope(Integer positionDataScope) {
        this.positionDataScope = positionDataScope;
    }

    public String getDataOfficeName(){
        if( CollectionUtil.isNotEmpty(officeListName)){
            return StringUtils.join(officeListName, Const.CHAR_COMMA);
        }
        return "";
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<String> getOfficeListName() {
        return officeListName;
    }

    public void setOfficeListName(List<String> officeListName) {
        this.officeListName = officeListName;
    }

}
