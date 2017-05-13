package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Client;

/**
 * Created by lihx on 2017/4/18.
 */
public class ClientForm extends DataForm<Client> {
    private String loginName;
    private String name;
    private String mobilePhone;
    private String password;
    private String confirmPassword;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
