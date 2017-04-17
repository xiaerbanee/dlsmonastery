package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.sys.dto.DictEnumDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class EmployeeForm extends DataForm<Employee> {
    private String code;
    private String mobilePhone;
    private LocalDate entryDate;
    private LocalDate regularDate;
    private LocalDate leaveDate;
    private String name;
    private BigDecimal salary;
    private String idcard;
    private LocalDate birthday;
    private String education;
    private String major;
    private String school;
    private String bankName;
    private String bankNumber;
    private String sex;
    private String originId;
    private String dutyCode;
    private String salerName;
    private List<PositionDto> positionDtoList;
    private List<DictEnumDto> educationsList;

    public List<PositionDto> getPositionDtoList() {
        return positionDtoList;
    }

    public void setPositionDtoList(List<PositionDto> positionDtoList) {
        this.positionDtoList = positionDtoList;
    }

    public List<DictEnumDto> getEducationsList() {
        return educationsList;
    }

    public void setEducationsList(List<DictEnumDto> educationsList) {
        this.educationsList = educationsList;
    }

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
}
