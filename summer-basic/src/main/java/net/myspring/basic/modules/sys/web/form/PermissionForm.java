package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.common.form.DataForm;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PermissionForm extends DataForm<Permission> {

    private List<String> roleIdList=Lists.newArrayList();
    private String menuId;
    private String name;
    private String permission;
    private String remarks;
    private String url;
    private String method;
    private List<MenuDto> menuList= Lists.newArrayList();
    @CacheInput(inputKey = "roles",inputInstance = "roleIdList",outputInstance = "name")
    private List<String> roleNameList=Lists.newArrayList();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public List<String> getRoleNameList() {
        return roleNameList;
    }

    public void setRoleNameList(List<String> roleNameList) {
        this.roleNameList = roleNameList;
    }

    public List<MenuDto> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuDto> menuList) {
        this.menuList = menuList;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
