package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.mybatis.form.BaseForm;

import java.util.List;

/**
 * Created by liuj on 2017/3/19.
 */

public class AccountForm extends BaseForm<Account>{
    private String id;
    private String password;
    private List<String> OfficeIdList;
    private String type;
    private String employeeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getOfficeIdList() {
        return OfficeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        OfficeIdList = officeIdList;
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
}
