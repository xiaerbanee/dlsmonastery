package net.myspring.basic.modules.salary.web.query;

import net.myspring.basic.common.query.BaseQuery;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.util.text.StringUtils;

public class SalaryQuery extends BaseQuery{
    private String employeeId;
    private String month;
    private String projectName;
    private String projectValue;
    private String employeeName;
    private String password;
    private String accountId;

    public String getAccountId() {
        if(StringUtils.isBlank(accountId)){
            this.accountId=RequestUtils.getAccountId();
        }
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        if(StringUtils.isBlank(employeeId)){
            this.employeeId=RequestUtils.getEmployeeId();
        }
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(String projectValue) {
        this.projectValue = projectValue;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
