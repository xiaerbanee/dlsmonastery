package net.myspring.basic.modules.hr.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "hr_employee")
public class Employee extends CompanyEntity<Employee> {
    private String code;
    private String mobilePhone;
    private String image;
    private LocalDate entryDate;
    private LocalDate regularDate;
    private LocalDate leaveDate;
    private String status;
    private String dingId;
    private String name;
    private BigDecimal salary;
    private Integer version = 0;
    private String idcard;
    private LocalDate birthday;
    private String education;
    private String major;
    private String school;
    private String bankName;
    private String bankItem;
    private String bankNumber;
    private String sex;
    private String originId;
    private String dutyCode;
    private String salerName;
    private List<Account> accountList = Lists.newArrayList();
    private List<String> accountIdList = Lists.newArrayList();
    private List<DutyAnnual> dutyAnnualList = Lists.newArrayList();
    private List<String> dutyAnnualIdList = Lists.newArrayList();
    private List<DutyFree> dutyFreeList = Lists.newArrayList();
    private List<String> dutyFreeIdList = Lists.newArrayList();
    private List<DutyLeave> dutyLeaveList = Lists.newArrayList();
    private List<String> dutyLeaveIdList = Lists.newArrayList();
    private List<DutyOvertime> dutyOvertimeList = Lists.newArrayList();
    private List<String> dutyOvertimeIdList = Lists.newArrayList();
    private List<DutyPublicFree> dutyPublicFreeList = Lists.newArrayList();
    private List<String> dutyPublicFreeIdList = Lists.newArrayList();
    private List<DutyRest> dutyRestList = Lists.newArrayList();
    private List<String> dutyRestIdList = Lists.newArrayList();
    private List<DutySign> dutySignList = Lists.newArrayList();
    private List<String> dutySignIdList = Lists.newArrayList();
    private List<DutyTrip> dutyTripList = Lists.newArrayList();
    private List<String> dutyTripIdList = Lists.newArrayList();
    private List<DutyWorktime> dutyWorktimeList = Lists.newArrayList();
    private List<String> dutyWorktimeIdList = Lists.newArrayList();
    private Account account;
    private String accountId;
    private List<EmployeePoint> employeePointList = Lists.newArrayList();
    private List<String> employeePointIdList = Lists.newArrayList();
    private List<EmployeeSalary> employeeSalaryList = Lists.newArrayList();
    private List<String> employeeSalaryIdList = Lists.newArrayList();
    private List<EmployeeSalaryBasic> employeeSalaryBasicList = Lists.newArrayList();
    private List<String> employeeSalaryBasicIdList = Lists.newArrayList();
    private List<Salary> salaryList = Lists.newArrayList();
    private List<String> salaryIdList = Lists.newArrayList();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getRegularDate() {
        return regularDate;
    }

    public void setRegularDate(LocalDate regularDate) {
        this.regularDate = regularDate;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDingId() {
        return dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankItem() {
        return bankItem;
    }

    public void setBankItem(String bankItem) {
        this.bankItem = bankItem;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }


    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<DutyAnnual> getDutyAnnualList() {
        return dutyAnnualList;
    }

    public void setDutyAnnualList(List<DutyAnnual> dutyAnnualList) {
        this.dutyAnnualList = dutyAnnualList;
    }

    public List<String> getDutyAnnualIdList() {
        return dutyAnnualIdList;
    }

    public void setDutyAnnualIdList(List<String> dutyAnnualIdList) {
        this.dutyAnnualIdList = dutyAnnualIdList;
    }

    public List<DutyFree> getDutyFreeList() {
        return dutyFreeList;
    }

    public void setDutyFreeList(List<DutyFree> dutyFreeList) {
        this.dutyFreeList = dutyFreeList;
    }

    public List<String> getDutyFreeIdList() {
        return dutyFreeIdList;
    }

    public void setDutyFreeIdList(List<String> dutyFreeIdList) {
        this.dutyFreeIdList = dutyFreeIdList;
    }

    public List<DutyLeave> getDutyLeaveList() {
        return dutyLeaveList;
    }

    public void setDutyLeaveList(List<DutyLeave> dutyLeaveList) {
        this.dutyLeaveList = dutyLeaveList;
    }

    public List<String> getDutyLeaveIdList() {
        return dutyLeaveIdList;
    }

    public void setDutyLeaveIdList(List<String> dutyLeaveIdList) {
        this.dutyLeaveIdList = dutyLeaveIdList;
    }

    public List<DutyOvertime> getDutyOvertimeList() {
        return dutyOvertimeList;
    }

    public void setDutyOvertimeList(List<DutyOvertime> dutyOvertimeList) {
        this.dutyOvertimeList = dutyOvertimeList;
    }

    public List<String> getDutyOvertimeIdList() {
        return dutyOvertimeIdList;
    }

    public void setDutyOvertimeIdList(List<String> dutyOvertimeIdList) {
        this.dutyOvertimeIdList = dutyOvertimeIdList;
    }

    public List<DutyPublicFree> getDutyPublicFreeList() {
        return dutyPublicFreeList;
    }

    public void setDutyPublicFreeList(List<DutyPublicFree> dutyPublicFreeList) {
        this.dutyPublicFreeList = dutyPublicFreeList;
    }

    public List<String> getDutyPublicFreeIdList() {
        return dutyPublicFreeIdList;
    }

    public void setDutyPublicFreeIdList(List<String> dutyPublicFreeIdList) {
        this.dutyPublicFreeIdList = dutyPublicFreeIdList;
    }

    public List<DutyRest> getDutyRestList() {
        return dutyRestList;
    }

    public void setDutyRestList(List<DutyRest> dutyRestList) {
        this.dutyRestList = dutyRestList;
    }

    public List<String> getDutyRestIdList() {
        return dutyRestIdList;
    }

    public void setDutyRestIdList(List<String> dutyRestIdList) {
        this.dutyRestIdList = dutyRestIdList;
    }

    public List<DutySign> getDutySignList() {
        return dutySignList;
    }

    public void setDutySignList(List<DutySign> dutySignList) {
        this.dutySignList = dutySignList;
    }

    public List<String> getDutySignIdList() {
        return dutySignIdList;
    }

    public void setDutySignIdList(List<String> dutySignIdList) {
        this.dutySignIdList = dutySignIdList;
    }

    public List<DutyTrip> getDutyTripList() {
        return dutyTripList;
    }

    public void setDutyTripList(List<DutyTrip> dutyTripList) {
        this.dutyTripList = dutyTripList;
    }

    public List<String> getDutyTripIdList() {
        return dutyTripIdList;
    }

    public void setDutyTripIdList(List<String> dutyTripIdList) {
        this.dutyTripIdList = dutyTripIdList;
    }

    public List<DutyWorktime> getDutyWorktimeList() {
        return dutyWorktimeList;
    }

    public void setDutyWorktimeList(List<DutyWorktime> dutyWorktimeList) {
        this.dutyWorktimeList = dutyWorktimeList;
    }

    public List<String> getDutyWorktimeIdList() {
        return dutyWorktimeIdList;
    }

    public void setDutyWorktimeIdList(List<String> dutyWorktimeIdList) {
        this.dutyWorktimeIdList = dutyWorktimeIdList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<EmployeePoint> getEmployeePointList() {
        return employeePointList;
    }

    public void setEmployeePointList(List<EmployeePoint> employeePointList) {
        this.employeePointList = employeePointList;
    }

    public List<String> getEmployeePointIdList() {
        return employeePointIdList;
    }

    public void setEmployeePointIdList(List<String> employeePointIdList) {
        this.employeePointIdList = employeePointIdList;
    }

    public List<EmployeeSalary> getEmployeeSalaryList() {
        return employeeSalaryList;
    }

    public void setEmployeeSalaryList(List<EmployeeSalary> employeeSalaryList) {
        this.employeeSalaryList = employeeSalaryList;
    }

    public List<String> getEmployeeSalaryIdList() {
        return employeeSalaryIdList;
    }

    public void setEmployeeSalaryIdList(List<String> employeeSalaryIdList) {
        this.employeeSalaryIdList = employeeSalaryIdList;
    }

    public List<EmployeeSalaryBasic> getEmployeeSalaryBasicList() {
        return employeeSalaryBasicList;
    }

    public void setEmployeeSalaryBasicList(List<EmployeeSalaryBasic> employeeSalaryBasicList) {
        this.employeeSalaryBasicList = employeeSalaryBasicList;
    }

    public List<String> getEmployeeSalaryBasicIdList() {
        return employeeSalaryBasicIdList;
    }

    public void setEmployeeSalaryBasicIdList(List<String> employeeSalaryBasicIdList) {
        this.employeeSalaryBasicIdList = employeeSalaryBasicIdList;
    }

    public List<Salary> getSalaryList() {
        return salaryList;
    }

    public void setSalaryList(List<Salary> salaryList) {
        this.salaryList = salaryList;
    }

    public List<String> getSalaryIdList() {
        return salaryIdList;
    }

    public void setSalaryIdList(List<String> salaryIdList) {
        this.salaryIdList = salaryIdList;
    }
}
