package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */

public class AccountForm extends DataForm<Account>{
    private String password;
    private List<String> officeIdList;
    private String type;
    private String employeeId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String loginName;
    private String officeId;
    private String leaderId;
    private Boolean viewReport;
    private String outId;
    private String outPassword;
    private String remarks;
    private String positionId;
    private List<PositionDto> positionDtoList= Lists.newArrayList();
    private Map<Boolean,String> bools= Maps.newHashMap();

    public Boolean getViewReport() {
        return viewReport;
    }

    public List<PositionDto> getPositionDtoList() {
        return positionDtoList;
    }

    public void setPositionDtoList(List<PositionDto> positionDtoList) {
        this.positionDtoList = positionDtoList;
    }

    public Map<Boolean, String> getBools() {
        return bools;
    }

    public void setBools(Map<Boolean, String> bools) {
        this.bools = bools;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
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

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public Boolean isViewReport() {
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}
