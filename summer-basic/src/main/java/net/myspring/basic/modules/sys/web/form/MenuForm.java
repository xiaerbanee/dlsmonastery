package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Menu;

/**
 * Created by admin on 2017/4/5.
 */
public class MenuForm extends Menu {
    private String permissionStr;

    public String getPermissionStr() {
        return permissionStr;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }
}
