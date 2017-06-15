package net.myspring.uaa.web.form;

/**
 * Created by wangzm on 2017/4/20.
 */
public class AccountWeixinForm {
    private String code;
    private String loginName;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
