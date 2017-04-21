package net.myspring.basic.modules.hr.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.basic.modules.sys.domain.Menu;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name="hr_account")
public class Account extends CompanyEntity<Account> {
    private String loginName;
    private Integer version = 0;
    private String password;
    private String leaderId;
    private Account leader;
    private String type;
    private String outId;
    private String outPassword;
    private Boolean viewReport;
    private Position position;
    private String positionId;
    private Office office;
    private String officeId;
    private Employee employee;
    private String employeeId;
    private String dealerId;
    private List<AccountChange> accountChangeList = Lists.newArrayList();
    private List<String> accountChangeIdList = Lists.newArrayList();
    private List<Menu> menuList = Lists.newArrayList();
    private List<String> menuIdList = Lists.newArrayList();
    private List<Office> officeList = Lists.newArrayList();
    private List<String> officeIdList = Lists.newArrayList();
    private List<String> accountTaskIdList = Lists.newArrayList();
    private List<Employee> employeeList = Lists.newArrayList();
    private List<String> employeeIdList = Lists.newArrayList();
    private List<String> monitorIdList = Lists.newArrayList();

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public Account getLeader() {
        return leader;
    }

    public void setLeader(Account leader) {
        this.leader = leader;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getViewReport() {
        return viewReport;
    }

    public void setViewReport(Boolean viewReport) {
        this.viewReport = viewReport;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public List<AccountChange> getAccountChangeList() {
        return accountChangeList;
    }

    public void setAccountChangeList(List<AccountChange> accountChangeList) {
        this.accountChangeList = accountChangeList;
    }

    public List<String> getAccountChangeIdList() {
        return accountChangeIdList;
    }

    public void setAccountChangeIdList(List<String> accountChangeIdList) {
        this.accountChangeIdList = accountChangeIdList;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<String> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        this.menuIdList = menuIdList;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<String> getAccountTaskIdList() {
        return accountTaskIdList;
    }

    public void setAccountTaskIdList(List<String> accountTaskIdList) {
        this.accountTaskIdList = accountTaskIdList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<String> getEmployeeIdList() {
        return employeeIdList;
    }

    public void setEmployeeIdList(List<String> employeeIdList) {
        this.employeeIdList = employeeIdList;
    }

    public List<String> getMonitorIdList() {
        return monitorIdList;
    }

    public void setMonitorIdList(List<String> monitorIdList) {
        this.monitorIdList = monitorIdList;
    }
}
