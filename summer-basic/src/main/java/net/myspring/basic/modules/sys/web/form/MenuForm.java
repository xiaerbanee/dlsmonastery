package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.mybatis.annotation.FormDomain;

/**
 * Created by admin on 2017/4/5.
 */
@FormDomain(MenuForm.class)
public class MenuForm {
    private String permissionStr;

    public String getPermissionStr() {
        return permissionStr;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }
}
