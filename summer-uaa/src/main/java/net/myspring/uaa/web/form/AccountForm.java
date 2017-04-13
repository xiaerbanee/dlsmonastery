package net.myspring.uaa.web.form;

import net.myspring.mybatis.form.BaseForm;

/**
 * Created by zhucc on 2017/4/12.
 */
public class AccountForm  {

    private String loginName;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
