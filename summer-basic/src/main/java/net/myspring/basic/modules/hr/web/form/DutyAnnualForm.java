package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutyAnnual;
import net.myspring.common.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyAnnualForm extends BaseForm<DutyAnnual> {
    private String employeeName;
    private String loginName;
    private String hour;
    private String leftHour;
    private String employeeId;
    private String annualYear;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAnnualYear() {
        return annualYear;
    }

    public void setAnnualYear(String annualYear) {
        this.annualYear = annualYear;
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getLeftHour() {
        return leftHour;
    }

    public void setLeftHour(String leftHour) {
        this.leftHour = leftHour;
    }
}
